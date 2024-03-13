package com.ryanmoelter.magellanx.test

import com.ryanmoelter.magellanx.compose.navigation.ComposeNavigationEvent
import com.ryanmoelter.magellanx.compose.navigation.ComposeNavigator
import com.ryanmoelter.magellanx.compose.navigation.Direction
import com.ryanmoelter.magellanx.core.Navigable
import com.ryanmoelter.magellanx.core.navigation.LinearNavigator

/**
 * An implementation of [LinearNavigator] suitable for tests. Avoids attaching child [Navigable]s to
 * the lifecycle, avoiding the need to satisfy their dependencies. Holds state; should be
 * re-instantiated, [destroy]ed, or [clear]ed between tests.
 */
public class FakeComposeNavigator : ComposeNavigator() {
  public fun setBackStack(backStack: List<ComposeNavigationEvent>) {
    backStackFlow.value = backStack
  }

  /**
   * Clear this navigator for the next test. [destroy] will do the same thing, and it's also safe to
   * leave this object to be garbage collected instead.
   */
  public fun clear() {
    setBackStack(emptyList())
  }

  public override fun navigate(
    direction: Direction,
    backStackOperation: (List<ComposeNavigationEvent>) -> List<ComposeNavigationEvent>,
  ) {
    setBackStack(backStackOperation(backStack))
  }

  public override fun onDestroy() {
    clear()
  }
}
