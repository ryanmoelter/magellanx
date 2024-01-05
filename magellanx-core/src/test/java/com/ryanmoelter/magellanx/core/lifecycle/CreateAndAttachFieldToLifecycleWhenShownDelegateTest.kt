package com.ryanmoelter.magellanx.core.lifecycle

import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.junit.Before
import org.junit.Test

public class CreateAndAttachFieldToLifecycleWhenShownDelegateTest {
  private lateinit var lifecycleView: CreateAndAttachFieldToLifecycleWhenShownDelegate<Int>

  @Before
  public fun setUp() {
    lifecycleView = CreateAndAttachFieldToLifecycleWhenShownDelegate { 1 }
  }

  @Test
  public fun wholeLifecycle() {
    lifecycleView.create()
    lifecycleView.field.shouldBeNull()

    lifecycleView.show()
    lifecycleView.field shouldBe 1

    lifecycleView.resume()
    lifecycleView.field shouldBe 1

    lifecycleView.pause()
    lifecycleView.field shouldBe 1

    lifecycleView.hide()
    lifecycleView.field.shouldBeNull()

    lifecycleView.destroy()
    lifecycleView.field.shouldBeNull()
  }

  @Test
  public fun onCreateView() {
    lifecycleView.show()
    lifecycleView.field shouldBe 1
  }

  @Test
  public fun onDestroyView() {
    lifecycleView.hide()
    lifecycleView.field.shouldBeNull()
  }
}
