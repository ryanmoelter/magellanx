package com.ryanmoelter.magellanx.core.debug

import com.ryanmoelter.magellanx.core.lifecycle.LifecycleAware
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleAwareComponent
import io.kotest.matchers.shouldBe
import org.junit.Before
import org.junit.Test

public class StatePrinterTest {
  private lateinit var root: LifecycleAwareComponent

  @Before
  public fun setUp() {
    root = DummyLifecycleOwner()
  }

  @Test
  public fun singleItem() {
    root.getLifecycleStateSnapshot() shouldBe "DummyLifecycleOwner (Destroyed)\n"
  }

  @Test
  @Suppress("ktlint:standard:string-template-indent")
  public fun singleChild() {
    root.attachToLifecycle(MyStep())

    root.getLifecycleStateSnapshot() shouldBe """
      DummyLifecycleOwner (Destroyed)
      └ MyStep (Destroyed)
    """.trimIndent() + '\n'
  }

  @Test
  @Suppress("ktlint:standard:string-template-indent")
  public fun multipleChildren() {
    root.attachToLifecycle(MyStep())
    root.attachToLifecycle(MyStep())
    root.create()
    root.show()

    root.getLifecycleStateSnapshot() shouldBe """
      DummyLifecycleOwner (Shown)
      ├ MyStep (Shown)
      └ MyStep (Shown)
    """.trimIndent() + '\n'
  }

  @Test
  @Suppress("ktlint:standard:string-template-indent")
  public fun complexTree() {
    root.attachToLifecycle(MyStep())
    val step = MyStep()
    root.attachToLifecycle(MyJourney().apply { attachToLifecycle(step) })
    step.attachToLifecycle(MySection())
    root.attachToLifecycle(MyJourney().apply { attachToLifecycle(MyLifecycleAwareThing()) })
    root.create()

    root.getLifecycleStateSnapshot() shouldBe """
      DummyLifecycleOwner (Created)
      ├ MyStep (Created)
      ├ MyJourney (Created)
      | └ MyStep (Created)
      |   └ MySection (Created)
      └ MyJourney (Created)
        └ MyLifecycleAwareThing (Created?)
    """.trimIndent() + '\n'
  }
}

private class DummyLifecycleOwner : LifecycleAwareComponent()

private class MyJourney : LifecycleAwareComponent()

private class MyStep : LifecycleAwareComponent()

private class MySection : LifecycleAwareComponent()

private class MyLifecycleAwareThing : LifecycleAware
