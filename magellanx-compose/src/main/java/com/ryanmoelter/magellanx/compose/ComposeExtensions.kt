package com.ryanmoelter.magellanx.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.ryanmoelter.magellanx.core.Displayable
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleAware
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleOwner
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Resumed
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Started
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@Composable
@Suppress("ktlint:standard:function-naming")
public fun Displayable<@Composable () -> Unit>.Content(modifier: Modifier = Modifier) {
  Box(modifier = modifier) {
    view!!()
  }
}

@Composable
@Suppress("ktlint:standard:function-naming")
public fun <Arg> Displayable<
  @Composable (
    Arg,
  ) -> Unit,
>.Content(
  arg: Arg,
  modifier: Modifier = Modifier,
) {
  Box(modifier = modifier) {
    view!!(arg)
  }
}

@Composable
@Suppress("ktlint:standard:function-naming")
public fun Displayable(
  displayable: Displayable<@Composable () -> Unit>,
  modifier: Modifier = Modifier,
): Unit = displayable.Content(modifier)

@Composable
@Suppress("ktlint:standard:function-naming")
public fun LifecycleOwner.WhenStarted(content: @Composable () -> Unit) {
  val isStartedFlow =
    remember {
      currentStateFlow
        .map { lifecycleState -> lifecycleState >= Started }
    }
  val isStarted by isStartedFlow.collectAsState(false)
  if (isStarted) {
    content()
  }
}

@Composable
@Suppress("ktlint:standard:function-naming")
public fun LifecycleOwner.WhenResumed(content: @Composable () -> Unit) {
  val isResumedFlow =
    remember {
      currentStateFlow
        .map { lifecycleState -> lifecycleState >= Resumed }
    }
  val isResumed by isResumedFlow.collectAsState(false)
  if (isResumed) {
    content()
  }
}

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
public fun <T, L> StateFlow<T>.collectAsStateWhileResumed(
  lifecycleComponent: L,
  context: CoroutineContext = EmptyCoroutineContext,
): State<T> where L : LifecycleAware, L : LifecycleOwner =
  collectAsStateWhileResumed(value, lifecycleComponent, context)

/**
 * Like [collectAsState], but stops emitting values when this ComposeStep is not Resumed. This is
 * primarily useful for preventing values from updating during transitions.
 *
 * Note that this keeps a subscription to the underlying [Flow] even while not Resumed; any
 * emitted values are simply ignored until this ComposeStep is Resumed, at which point the most
 * recent value will be emitted before any subsequent values.
 */
@Composable
public fun <T : R, R, L> Flow<T>.collectAsStateWhileResumed(
  initial: R,
  lifecycleComponent: L,
  context: CoroutineContext = EmptyCoroutineContext,
): State<R> where L : LifecycleAware, L : LifecycleOwner =
  produceState(initial, this, context) {
    if (context == EmptyCoroutineContext) {
      combine(lifecycleComponent.currentStateFlow) { it, currentState -> it to currentState }
        .collect { (it, currentState) ->
          if (currentState == Resumed) {
            value = it
          }
        }
    } else {
      withContext(context) {
        combine(lifecycleComponent.currentStateFlow) { it, currentState -> it to currentState }
          .collect { (it, currentState) ->
            if (currentState == Resumed) {
              value = it
            }
          }
      }
    }
  }
