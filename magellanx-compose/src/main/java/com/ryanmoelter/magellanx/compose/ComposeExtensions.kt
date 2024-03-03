package com.ryanmoelter.magellanx.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.ryanmoelter.magellanx.core.Displayable
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleOwner
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Resumed
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Started
import kotlinx.coroutines.flow.map

@Composable
@Suppress("ktlint:standard:function-naming")
public fun Displayable<@Composable () -> Unit>.Content(modifier: Modifier = Modifier) {
  Box(modifier = modifier) {
    view!!()
  }
}

@Composable
@Suppress("ktlint:standard:function-naming")
public fun <Arg> Displayable<@Composable (Arg) -> Unit>.Content(arg: Arg, modifier: Modifier = Modifier) {
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
