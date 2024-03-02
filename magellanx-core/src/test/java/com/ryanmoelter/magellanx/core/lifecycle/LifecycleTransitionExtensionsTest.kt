package com.ryanmoelter.magellanx.core.lifecycle

import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Created
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Destroyed
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Resumed
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Shown
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Started
import com.ryanmoelter.magellanx.core.lifecycle.VerifyingLifecycleAware.LifecycleEvent.CREATE
import com.ryanmoelter.magellanx.core.lifecycle.VerifyingLifecycleAware.LifecycleEvent.DESTROY
import com.ryanmoelter.magellanx.core.lifecycle.VerifyingLifecycleAware.LifecycleEvent.HIDE
import com.ryanmoelter.magellanx.core.lifecycle.VerifyingLifecycleAware.LifecycleEvent.PAUSE
import com.ryanmoelter.magellanx.core.lifecycle.VerifyingLifecycleAware.LifecycleEvent.RESUME
import com.ryanmoelter.magellanx.core.lifecycle.VerifyingLifecycleAware.LifecycleEvent.SHOW
import com.ryanmoelter.magellanx.core.lifecycle.VerifyingLifecycleAware.LifecycleEvent.START
import com.ryanmoelter.magellanx.core.lifecycle.VerifyingLifecycleAware.LifecycleEvent.STOP
import io.kotest.matchers.collections.shouldBeEmpty
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
  fun transitionBetweenLifecycleStates_createdToCreated() {
    lifecycleAware.transition(Created, Created)

    lifecycleAware.events shouldBe emptyList()
  }

  @Test
  fun transitionBetweenLifecycleStates_createdToShown() {
    lifecycleAware.transition(Created, Shown)

    lifecycleAware.events shouldBe listOf(SHOW)
  }

  @Test
  fun transitionBetweenLifecycleStates_createdToStarted() {
    lifecycleAware.transition(Created, Started)

    lifecycleAware.events shouldBe listOf(SHOW, START)
  }

  @Test
  fun transitionBetweenLifecycleStates_createdToResumed() {
    lifecycleAware.transition(Created, Resumed)

    lifecycleAware.events shouldBe listOf(SHOW, START, RESUME)
  }

  @Test
  fun transitionBetweenLifecycleStates_createdToDestroyed() {
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
  fun transitionBetweenLifecycleStates_shownToStarted() {
    lifecycleAware.transition(Shown, Started)

    lifecycleAware.events shouldBe listOf(START)
  }

  @Test
  fun transitionBetweenLifecycleStates_shownToResumed() {
    lifecycleAware.transition(Shown, Resumed)

    lifecycleAware.events shouldBe listOf(START, RESUME)
  }

  @Test
  fun transitionBetweenLifecycleStates_shownToDestroyed() {
    lifecycleAware.transition(Shown, Destroyed)

    lifecycleAware.events shouldBe listOf(HIDE, DESTROY)
  }

  @Test
  fun transitionBetweenLifecycleStates_startedToCreated() {
    lifecycleAware.transition(Started, Created)

    lifecycleAware.events shouldBe listOf(STOP, HIDE)
  }

  @Test
  fun transitionBetweenLifecycleStates_startedToShown() {
    lifecycleAware.transition(Started, Shown)

    lifecycleAware.events shouldBe listOf(STOP)
  }

  @Test
  fun transitionBetweenLifecycleStates_startedToStarted() {
    lifecycleAware.transition(Started, Started)

    lifecycleAware.events.shouldBeEmpty()
  }

  @Test
  fun transitionBetweenLifecycleStates_startedToResumed() {
    lifecycleAware.transition(Started, Resumed)

    lifecycleAware.events shouldBe listOf(RESUME)
  }

  @Test
  fun transitionBetweenLifecycleStates_startedToDestroyed() {
    lifecycleAware.transition(Started, Destroyed)

    lifecycleAware.events shouldBe listOf(STOP, HIDE, DESTROY)
  }

  @Test
  fun transitionBetweenLifecycleStates_resumedToCreated() {
    lifecycleAware.transition(Resumed, Created)

    lifecycleAware.events shouldBe listOf(PAUSE, STOP, HIDE)
  }

  @Test
  fun transitionBetweenLifecycleStates_resumedToShown() {
    lifecycleAware.transition(Resumed, Shown)

    lifecycleAware.events shouldBe listOf(PAUSE, STOP)
  }

  @Test
  fun transitionBetweenLifecycleStates_resumedToStarted() {
    lifecycleAware.transition(Resumed, Started)

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

    lifecycleAware.events shouldBe listOf(PAUSE, STOP, HIDE, DESTROY)
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
  fun transitionBetweenLifecycleStates_destroyedToStarted() {
    lifecycleAware.transition(Destroyed, Started)

    lifecycleAware.events shouldBe listOf(CREATE, SHOW, START)
  }

  @Test
  fun transitionBetweenLifecycleStates_destroyedToResumed() {
    lifecycleAware.transition(Destroyed, Resumed)

    lifecycleAware.events shouldBe listOf(CREATE, SHOW, START, RESUME)
  }

  @Test
  fun transitionBetweenLifecycleStates_destroyedToDestroyed() {
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

  override fun start() {
    events += START
  }

  override fun resume() {
    events += RESUME
  }

  override fun pause() {
    events += PAUSE
  }

  override fun stop() {
    events += STOP
  }

  override fun hide() {
    events += HIDE
  }

  override fun destroy() {
    events += DESTROY
  }

  enum class LifecycleEvent {
    CREATE,
    SHOW,
    START,
    RESUME,
    PAUSE,
    STOP,
    HIDE,
    DESTROY,
  }
}
