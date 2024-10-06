package com.ryanmoelter.magellanx.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ryanmoelter.magellanx.core.Displayable
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleOwner
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Resumed
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Started

@Composable
@Suppress("ktlint:standard:function-naming")
public fun Displayable<@Composable () -> Unit>.Content(modifier: Modifier = Modifier) {
  Box(modifier = modifier) {
    view!!()
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
  val currentState by currentStateFlow.collectAsState()
  val isStarted = currentState >= Started
  if (isStarted) {
    content()
  }
}

@Composable
@Suppress("ktlint:standard:function-naming")
public fun LifecycleOwner.WhenResumed(content: @Composable () -> Unit) {
  val currentState by currentStateFlow.collectAsState()
  val isResumed = currentState >= Resumed
  if (isResumed) {
    content()
  }
}
