package com.ryanmoelter.magellanx.test

import com.ryanmoelter.magellanx.compose.navigation.ComposeNavigationEvent
import com.ryanmoelter.magellanx.compose.navigation.Direction
import com.ryanmoelter.magellanx.compose.transitions.defaultTransition
import com.ryanmoelter.magellanx.core.init.Magellan
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState
import com.ryanmoelter.magellanx.core.lifecycle.transitionToState
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.types.shouldBeSameInstanceAs
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test

public class FakeLinearNavigatorTest {
  private lateinit var navigator: FakeComposeNavigator

  @Before
  public fun setUp() {
    Magellan.init(mainDispatcher = Dispatchers.Unconfined)
    navigator = FakeComposeNavigator()
  }

  @Test
  public fun clear() {
    navigator.setBackStack(listOf(ComposeNavigationEvent(TestNavigable(), defaultTransition)))
    navigator.clear()
    navigator.backStack.shouldBeEmpty()
    navigator.currentNavigable.shouldBeNull()
  }

  @Test
  public fun goTo() {
    navigator.goTo(TestNavigable())
    navigator.backStack shouldHaveSize 1

    val topNavigable = TestNavigable()
    navigator.goTo(topNavigable)
    navigator.backStack shouldHaveSize 2
    navigator.currentNavigable shouldBeSameInstanceAs topNavigable
  }

  @Test
  public fun replace() {
    navigator.goTo(TestNavigable())
    navigator.backStack shouldHaveSize 1

    val topNavigable = TestNavigable()
    navigator.replace(topNavigable)
    navigator.backStack shouldHaveSize 1
    navigator.currentNavigable shouldBeSameInstanceAs topNavigable
  }

  @Test
  public fun navigate() {
    val originalBottomNavigable = TestNavigable()
    val newBottomNavigable = TestNavigable()
    val newTopNavigable = TestNavigable()
    navigator.setBackStack(
      listOf(
        ComposeNavigationEvent(originalBottomNavigable, defaultTransition),
        ComposeNavigationEvent(TestNavigable(), defaultTransition),
      ),
    )

    navigator.navigate(Direction.FORWARD) {
      listOf(
        ComposeNavigationEvent(newBottomNavigable, defaultTransition),
        ComposeNavigationEvent(originalBottomNavigable, defaultTransition),
        ComposeNavigationEvent(newTopNavigable, defaultTransition),
      )
    }
    navigator.backStack.map { it.navigable } shouldContainExactly
      listOf(
        newBottomNavigable,
        originalBottomNavigable,
        newTopNavigable,
      )
    navigator.currentNavigable shouldBeSameInstanceAs newTopNavigable
  }

  @Test
  public fun goBack_noop() {
    navigator.goBack().shouldBeFalse()
    navigator.goTo(TestNavigable())
    navigator.goBack().shouldBeFalse()
    navigator.backStack shouldHaveSize 1
  }

  @Test
  public fun goBack_success() {
    val bottomNavigable = TestNavigable()
    navigator.setBackStack(
      listOf(
        ComposeNavigationEvent(bottomNavigable, defaultTransition),
        ComposeNavigationEvent(TestNavigable(), defaultTransition),
      ),
    )
    navigator.goBack().shouldBeTrue()
    navigator.backStack shouldHaveSize 1
    navigator.currentNavigable shouldBeSameInstanceAs bottomNavigable
  }

  @Test
  public fun destroy() {
    navigator.setBackStack(listOf(ComposeNavigationEvent(TestNavigable(), defaultTransition)))
    navigator.transitionToState(LifecycleState.Created)
    navigator.destroy()
    navigator.backStack.shouldBeEmpty()
    navigator.currentNavigable.shouldBeNull()
  }
}
