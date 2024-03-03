package com.ryanmoelter.magellanx.core.lifecycle

import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Created
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Destroyed
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Resumed
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Shown
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Started
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleStateDirection.BACKWARDS
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleStateDirection.FORWARD
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleStateDirection.NO_MOVEMENT
import io.kotest.matchers.shouldBe
import org.junit.Test

internal class LifecycleStateTest {
  @Test
  fun getTheDirectionIShouldGoToGetTo() {
    Destroyed.getDirectionForMovement(Destroyed) shouldBe NO_MOVEMENT
    Destroyed.getDirectionForMovement(Created) shouldBe FORWARD
    Destroyed.getDirectionForMovement(Shown) shouldBe FORWARD
    Destroyed.getDirectionForMovement(Started) shouldBe FORWARD
    Destroyed.getDirectionForMovement(Resumed) shouldBe FORWARD

    Created.getDirectionForMovement(Destroyed) shouldBe BACKWARDS
    Created.getDirectionForMovement(Created) shouldBe NO_MOVEMENT
    Created.getDirectionForMovement(Shown) shouldBe FORWARD
    Created.getDirectionForMovement(Started) shouldBe FORWARD
    Created.getDirectionForMovement(Resumed) shouldBe FORWARD

    Shown.getDirectionForMovement(Destroyed) shouldBe BACKWARDS
    Shown.getDirectionForMovement(Created) shouldBe BACKWARDS
    Shown.getDirectionForMovement(Shown) shouldBe NO_MOVEMENT
    Shown.getDirectionForMovement(Started) shouldBe FORWARD
    Shown.getDirectionForMovement(Resumed) shouldBe FORWARD

    Started.getDirectionForMovement(Destroyed) shouldBe BACKWARDS
    Started.getDirectionForMovement(Created) shouldBe BACKWARDS
    Started.getDirectionForMovement(Shown) shouldBe BACKWARDS
    Started.getDirectionForMovement(Started) shouldBe NO_MOVEMENT
    Started.getDirectionForMovement(Resumed) shouldBe FORWARD

    Resumed.getDirectionForMovement(Destroyed) shouldBe BACKWARDS
    Resumed.getDirectionForMovement(Created) shouldBe BACKWARDS
    Resumed.getDirectionForMovement(Shown) shouldBe BACKWARDS
    Resumed.getDirectionForMovement(Started) shouldBe BACKWARDS
    Resumed.getDirectionForMovement(Resumed) shouldBe NO_MOVEMENT
  }
}
