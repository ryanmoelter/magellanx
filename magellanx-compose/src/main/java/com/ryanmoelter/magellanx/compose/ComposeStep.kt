package com.ryanmoelter.magellanx.compose

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.produceState
import com.ryanmoelter.magellanx.core.Displayable
import com.ryanmoelter.magellanx.core.Navigable
import com.ryanmoelter.magellanx.core.coroutines.CreatedLifecycleScope
import com.ryanmoelter.magellanx.core.coroutines.ResumedLifecycleScope
import com.ryanmoelter.magellanx.core.coroutines.ShownLifecycleScope
import com.ryanmoelter.magellanx.core.coroutines.StartedLifecycleScope
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleAwareComponent
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState
import com.ryanmoelter.magellanx.core.lifecycle.attachFieldToLifecycle
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext

public abstract class ComposeStep :
  ComposeSection(),
  Navigable<@Composable () -> Unit>

public abstract class ComposeSection :
  LifecycleAwareComponent(),
  Displayable<@Composable () -> Unit> {
  override val view: (@Composable () -> Unit)?
    get() = {
      WhenStarted {
        Content()
      }
    }

  public var createdScope: CoroutineScope by attachFieldToLifecycle(CreatedLifecycleScope()) { it }
    @VisibleForTesting set

  public var shownScope: CoroutineScope by attachFieldToLifecycle(ShownLifecycleScope()) { it }
    @VisibleForTesting set

  public var startedScope: CoroutineScope by attachFieldToLifecycle(StartedLifecycleScope()) { it }
    @VisibleForTesting set

  public var resumedScope: CoroutineScope by attachFieldToLifecycle(ResumedLifecycleScope()) { it }
    @VisibleForTesting set

  @Composable
  @Suppress("ktlint:standard:function-naming")
  protected abstract fun Content()

  /**
   * Like [collectAsState], but stops emitting values when this ComposeStep is not Resumed. This is
   * primarily useful for preventing values from updating during transitions.
   *
   * Note that this keeps a subscription to the underlying [Flow] even while not Resumed; any
   * emitted values are simply ignored until this ComposeStep is Resumed, at which point the most
   * recent value will be emitted before any subsequent values.
   */
  @Suppress("StateFlowValueCalledInComposition")
  @Composable
  public fun <T> StateFlow<T>.collectAsStateWhileResumed(
    context: CoroutineContext = EmptyCoroutineContext,
  ): State<T> = collectAsStateWhileResumed(value, context)

  /**
   * Like [collectAsState], but stops emitting values when this ComposeStep is not Resumed. This is
   * primarily useful for preventing values from updating during transitions.
   *
   * Note that this keeps a subscription to the underlying [Flow] even while not Resumed; any
   * emitted values are simply ignored until this ComposeStep is Resumed, at which point the most
   * recent value will be emitted before any subsequent values.
   */
  @Composable
  public fun <T : R, R> Flow<T>.collectAsStateWhileResumed(
    initial: R,
    context: CoroutineContext = EmptyCoroutineContext,
  ): State<R> =
    @Suppress("ProduceStateDoesNotAssignValue")
    produceState(initial, this, context) {
      if (context == EmptyCoroutineContext) {
        combine(lifecycleRegistry.currentStateFlow) { it, currentState -> it to currentState }
          .collect { (it, currentState) ->
            if (currentState == LifecycleState.Resumed) {
              value = it
            }
          }
      } else {
        withContext(context) {
          combine(lifecycleRegistry.currentStateFlow) { it, currentState -> it to currentState }
            .collect { (it, currentState) ->
              if (currentState == LifecycleState.Resumed) {
                value = it
              }
            }
        }
      }
    }
}
