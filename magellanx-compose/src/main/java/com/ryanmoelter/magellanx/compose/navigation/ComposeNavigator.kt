@file:Suppress("ForbiddenComment")

package com.ryanmoelter.magellanx.compose.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ryanmoelter.magellanx.compose.Content
import com.ryanmoelter.magellanx.compose.WhenStarted
import com.ryanmoelter.magellanx.compose.navigation.Direction.BACKWARD
import com.ryanmoelter.magellanx.compose.navigation.Direction.FORWARD
import com.ryanmoelter.magellanx.compose.transitions.MagellanComposeTransition
import com.ryanmoelter.magellanx.compose.transitions.defaultTransition
import com.ryanmoelter.magellanx.compose.transitions.noTransition
import com.ryanmoelter.magellanx.core.Displayable
import com.ryanmoelter.magellanx.core.Navigable
import com.ryanmoelter.magellanx.core.coroutines.CreatedLifecycleScope
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleAwareComponent
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleLimit
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleOwner
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState
import com.ryanmoelter.magellanx.core.lifecycle.attachFieldToLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

public open class ComposeNavigator :
  LifecycleAwareComponent(), Displayable<@Composable () -> Unit> {
  private val createdScope by attachFieldToLifecycle(CreatedLifecycleScope())

  /**
   * The backstack. The last item in each list is the top of the stack.
   */
  protected val backStackFlow: MutableStateFlow<List<ComposeNavigationEvent>> =
    MutableStateFlow(emptyList())

  /**
   * Get a snapshot of the current item in [backStackFlow]. The last item is the top of the stack.
   */
  public val backStack: List<ComposeNavigationEvent>
    get() = backStackFlow.value

  /**
   * Get a snapshot of the current navigable, i.e. the last item of the current [backStack].
   */
  public val currentNavigable: Navigable<@Composable () -> Unit>?
    get() = backStack.lastOrNull()?.navigable

  public lateinit var currentNavigableFlow: StateFlow<Navigable<@Composable () -> Unit>?>
    private set

  // TODO: make default transition configurable
  private val transitionFlow: MutableStateFlow<MagellanComposeTransition> =
    MutableStateFlow(defaultTransition)
  private val directionFlow: MutableStateFlow<Direction> = MutableStateFlow(FORWARD)

  override fun onCreate() {
    currentNavigableFlow =
      backStackFlow.map { it.lastOrNull() }
        .map { it?.navigable }
        .stateIn(createdScope, SharingStarted.Eagerly, null)
  }

  override val view: (@Composable () -> Unit)
    get() = {
      WhenStarted {
        Content()
      }
    }

  @Composable
  @Suppress("ktlint:standard:function-naming")
  private fun Content() {
    val backStack by backStackFlow.collectAsState()
    val currentNavigable by currentNavigableFlow.collectAsState()
    val currentTransitionSpec by transitionFlow.collectAsState()
    val currentDirection by directionFlow.collectAsState()

    AnimatedContent(
      targetState = currentNavigable,
      transitionSpec = currentTransitionSpec.getTransitionForDirection(currentDirection),
      label = "Magellan Navigation transition",
    ) { navigable ->
      if (navigable != null) {
        LaunchedEffect(key1 = navigable, key2 = backStack) {
          if (backStack.lastOrNull()?.navigable == navigable) {
            // Navigable is Resumed if it's on the top of the backstack
            lifecycleRegistry.updateMaxState(navigable, LifecycleLimit.NO_LIMIT)
          } else {
            // Navigable is Started if it's in the composition, but not on top of the backstack
            lifecycleRegistry.updateMaxState(navigable, LifecycleLimit.STARTED)
          }
        }
        DisposableEffect(
          key1 = navigable,
          effect = {
            onDispose {
              // If the navigable is being removed from composition, it might not be attached
              // to this parent anymore
              if (children.contains(navigable)) {
                // Back events will remove the navigable from the backstack, but we keep it attached
                // to this parent until it's done animating out.
                when {
                  backStack.map { it.navigable }.last() == navigable -> {
                    // The navigable on top of the backstack is Shown if not visible
                    lifecycleRegistry.updateMaxState(navigable, LifecycleLimit.SHOWN)
                  }
                  backStack.map { it.navigable }.contains(navigable) -> {
                    // Any navigable still in the backstack but not visible is Created
                    lifecycleRegistry.updateMaxState(navigable, LifecycleLimit.CREATED)
                  }
                  else -> {
                    // Any navigable that's been removed from the backstack should also be removed
                    // from this LifecycleOwner
                    removeFromLifecycle(navigable)
                  }
                }
              }
            }
          },
        )
      }
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        navigable?.Content()
      }
    }
  }

  override fun onDestroy() {
    backStack
      .map { it.navigable }
      .forEach { lifecycleRegistry.removeFromLifecycle(it) }
    backStackFlow.value = emptyList()
  }

  public open fun goTo(
    navigable: Navigable<@Composable () -> Unit>,
    overrideTransitionSpec: MagellanComposeTransition? = null,
  ) {
    navigate(FORWARD) { backStack ->
      backStack +
        ComposeNavigationEvent(
          navigable = navigable,
          transitionSpec = overrideTransitionSpec ?: defaultTransition,
        )
    }
  }

  public open fun replace(
    navigable: Navigable<@Composable () -> Unit>,
    overrideTransitionSpec: MagellanComposeTransition? = null,
  ) {
    navigate(FORWARD) { backStack ->
      backStack - backStack.last() +
        ComposeNavigationEvent(
          navigable = navigable,
          transitionSpec = overrideTransitionSpec ?: defaultTransition,
        )
    }
  }

  public open fun goBack(): Boolean {
    return if (!atRoot()) {
      navigate(BACKWARD) { backStack ->
        backStack - backStack.last()
      }
      true
    } else {
      false
    }
  }

  public open fun goBackTo(navigable: Navigable<@Composable () -> Unit>) {
    navigate(BACKWARD) { backStack ->
      val mutableBackstack = backStack.toMutableList()
      while (navigable !== mutableBackstack.last().navigable) {
        mutableBackstack.removeLast()
      }
      mutableBackstack.toList()
    }
  }

  public open fun resetWithRoot(navigable: Navigable<@Composable () -> Unit>) {
    navigate(FORWARD) {
      listOf(ComposeNavigationEvent(navigable, noTransition))
    }
  }

  public open fun navigate(
    direction: Direction,
    backStackOperation: (backStack: List<ComposeNavigationEvent>) -> List<ComposeNavigationEvent>,
  ) {
    // TODO: Intercept touch events, if necessary
    NavigationPropagator._beforeNavigation.tryEmit(Unit)
    val oldBackStack = backStack
    val newBackStack = backStackOperation(backStack)
    val toNavigable = newBackStack.last().navigable
    directionFlow.value = direction
    transitionFlow.value =
      when (direction) {
        FORWARD -> newBackStack.last().transitionSpec
        BACKWARD -> oldBackStack.last().transitionSpec
      }
    findBackstackChangesAndUpdateStates(
      oldBackStack = oldBackStack.map { it.navigable },
      newBackStack = newBackStack.map { it.navigable },
    )
    /* Optimistically update toNavigable's limit to smooth out transitions. This will also get set
     * by the DisposedEffect in #Content(), but redundancy is fine. fromNavigable's limit will be
     * reset to CREATED in DisposedEffect.
     */
    lifecycleRegistry.updateMaxState(toNavigable, LifecycleLimit.NO_LIMIT)
    NavigationPropagator._onNavigatedTo.tryEmit(toNavigable)
    backStackFlow.value = newBackStack
    NavigationPropagator._afterNavigation.tryEmit(Unit)
  }

  private fun findBackstackChangesAndUpdateStates(
    oldBackStack: List<Navigable<*>>,
    newBackStack: List<Navigable<*>>,
  ) {
    val oldNavigables = oldBackStack.toSet()
    // Don't remove the last Navigable (from) until it's removed from composition in DisposedEffect
    val newNavigables = newBackStack.toSet()

    (oldNavigables - newNavigables).forEach { oldNavigable ->
      val isStarted =
        oldNavigable is LifecycleOwner && oldNavigable.currentState >= LifecycleState.Started
      if (!isStarted) {
        lifecycleRegistry.removeFromLifecycle(oldNavigable)
      }
    }

    (newNavigables - oldNavigables).forEach { newNavigable ->
      lifecycleRegistry.attachToLifecycleWithMaxState(newNavigable, LifecycleLimit.CREATED)
    }
  }

  override fun onBackPressed(): Boolean = currentNavigable?.backPressed() ?: false || goBack()

  public open fun atRoot(): Boolean = backStack.size <= 1
}

public data class ComposeNavigationEvent(
  val navigable: Navigable<@Composable () -> Unit>,
  val transitionSpec: MagellanComposeTransition,
)
