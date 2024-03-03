package com.ryanmoelter.magellanx.core.lifecycle

import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.junit.Before
import org.junit.Test

public class CreateAndAttachFieldToLifecycleWhenStartedDelegateTest {
  private lateinit var lifecycleView: CreateAndAttachFieldToLifecycleWhenStartedDelegate<Int>

  @Before
  public fun setUp() {
    lifecycleView = CreateAndAttachFieldToLifecycleWhenStartedDelegate { 1 }
  }

  @Test
  public fun wholeLifecycle() {
    lifecycleView.create()
    lifecycleView.field.shouldBeNull()

    lifecycleView.show()
    lifecycleView.field.shouldBeNull()

    lifecycleView.start()
    lifecycleView.field shouldBe 1

    lifecycleView.resume()
    lifecycleView.field shouldBe 1

    lifecycleView.pause()
    lifecycleView.field shouldBe 1

    lifecycleView.stop()
    lifecycleView.field.shouldBeNull()

    lifecycleView.hide()
    lifecycleView.field.shouldBeNull()

    lifecycleView.destroy()
    lifecycleView.field.shouldBeNull()
  }

  @Test
  public fun onCreateView() {
    lifecycleView.start()
    lifecycleView.field shouldBe 1
  }

  @Test
  public fun onDestroyView() {
    lifecycleView.stop()
    lifecycleView.field.shouldBeNull()
  }
}
