package com.ryanmoelter.magellanx.core.lifecycle

import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Created
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Destroyed
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Resumed
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Shown
import com.ryanmoelter.magellanx.core.lifecycle.VerifyingLifecycleAware.LifecycleEvent.CREATE
import com.ryanmoelter.magellanx.core.lifecycle.VerifyingLifecycleAware.LifecycleEvent.DESTROY
import com.ryanmoelter.magellanx.core.lifecycle.VerifyingLifecycleAware.LifecycleEvent.HIDE
import com.ryanmoelter.magellanx.core.lifecycle.VerifyingLifecycleAware.LifecycleEvent.PAUSE
import com.ryanmoelter.magellanx.core.lifecycle.VerifyingLifecycleAware.LifecycleEvent.RESUME
import com.ryanmoelter.magellanx.core.lifecycle.VerifyingLifecycleAware.LifecycleEvent.SHOW
import io.kotest.matchers.shouldBe
import org.junit.Before
import org.junit.Test

internal class LifecycleTransitionExtensionsTest {

  private lateinit var lifecycleAware: VerifyingLifecycleAware

  @Before
  fun setUp() {
    lifecycleAware = VerifyingLifecycleAware()
  }

  @Test
  fun transitionBetweenLifecycleStates_createToCreated() {
    lifecycleAware.transition(Created, Created)

    lifecycleAware.events shouldBe emptyList()
  }

  @Test
  fun transitionBetweenLifecycleStates_createToShown() {
    lifecycleAware.transition(Created, Shown)

    lifecycleAware.events shouldBe listOf(SHOW)
  }

  @Test
  fun transitionBetweenLifecycleStates_createToResumed() {
    lifecycleAware.transition(Created, Resumed)

    lifecycleAware.events shouldBe listOf(SHOW, RESUME)
  }

  @Test
  fun transitionBetweenLifecycleStates_createToDestroy() {
    lifecycleAware.transition(Created, Destroyed)

    lifecycleAware.events shouldBe listOf(DESTROY)
  }

  @Test
  fun transitionBetweenLifecycleStates_shownToCreated() {
    lifecycleAware.transition(Shown, Created)

    lifecycleAware.events shouldBe listOf(HIDE)
  }

  @Test
  fun transitionBetweenLifecycleStates_shownToShown() {
    lifecycleAware.transition(Shown, Shown)

    lifecycleAware.events shouldBe emptyList()
  }

  @Test
  fun transitionBetweenLifecycleStates_shownToResumed() {
    lifecycleAware.transition(Shown, Resumed)

    lifecycleAware.events shouldBe listOf(RESUME)
  }

  @Test
  fun transitionBetweenLifecycleStates_shownToDestroy() {
    lifecycleAware.transition(Shown, Destroyed)

    lifecycleAware.events shouldBe listOf(HIDE, DESTROY)
  }

  @Test
  fun transitionBetweenLifecycleStates_resumedToCreated() {
    lifecycleAware.transition(Resumed, Created)

    lifecycleAware.events shouldBe listOf(PAUSE, HIDE)
  }

  @Test
  fun transitionBetweenLifecycleStates_resumedToShown() {
    lifecycleAware.transition(Resumed, Shown)

    lifecycleAware.events shouldBe listOf(PAUSE)
  }

  @Test
  fun transitionBetweenLifecycleStates_resumedToResumed() {
    lifecycleAware.transition(Resumed, Resumed)

    lifecycleAware.events shouldBe emptyList()
  }

  @Test
  fun transitionBetweenLifecycleStates_resumedToDestroy() {
    lifecycleAware.transition(Resumed, Destroyed)

    lifecycleAware.events shouldBe listOf(PAUSE, HIDE, DESTROY)
  }

  @Test
  fun transitionBetweenLifecycleStates_destroyedToCreated() {
    lifecycleAware.transition(Destroyed, Created)

    lifecycleAware.events shouldBe listOf(CREATE)
  }

  @Test
  fun transitionBetweenLifecycleStates_destroyedToShown() {
    lifecycleAware.transition(Destroyed, Shown)

    lifecycleAware.events shouldBe listOf(CREATE, SHOW)
  }

  @Test
  fun transitionBetweenLifecycleStates_destroyedToResumed() {
    lifecycleAware.transition(Destroyed, Resumed)

    lifecycleAware.events shouldBe listOf(CREATE, SHOW, RESUME)
  }

  @Test
  fun transitionBetweenLifecycleStates_destroyedToDestroy() {
    lifecycleAware.transition(Destroyed, Destroyed)

    lifecycleAware.events shouldBe emptyList()
  }
}

private class VerifyingLifecycleAware : LifecycleAware {

  var events: List<LifecycleEvent> = emptyList()

  override fun create() {
    events += CREATE
  }

  override fun show() {
    events += SHOW
  }

  override fun resume() {
    events += RESUME
  }

  override fun pause() {
    events += PAUSE
  }

  override fun hide() {
    events += HIDE
  }

  override fun destroy() {
    events += DESTROY
  }

  enum class LifecycleEvent {
    CREATE, SHOW, RESUME, PAUSE, HIDE, DESTROY
  }
}
