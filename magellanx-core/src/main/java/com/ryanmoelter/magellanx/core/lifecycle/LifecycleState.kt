package com.ryanmoelter.magellanx.core.lifecycle

import com.ryanmoelter.magellanx.core.lifecycle.LifecycleStateDirection.BACKWARDS
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleStateDirection.FORWARD
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleStateDirection.NO_MOVEMENT

public enum class LifecycleState {
  Destroyed,
  Created,
  Shown,
  Resumed,
  ;

  internal fun getDirectionForMovement(other: LifecycleState) =
    when {
      this > other -> BACKWARDS
      this == other -> NO_MOVEMENT
      this < other -> FORWARD
      else -> throw IllegalStateException(
        "Unhandled order comparison: this is $this and other is $other",
      )
    }
}

public enum class LifecycleStateDirection {
  FORWARD,
  BACKWARDS,
  NO_MOVEMENT,
}
