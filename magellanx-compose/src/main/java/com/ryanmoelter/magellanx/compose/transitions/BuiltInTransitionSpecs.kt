package com.ryanmoelter.magellanx.compose.transitions

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import com.ryanmoelter.magellanx.compose.navigation.Direction
import com.ryanmoelter.magellanx.core.Navigable
import kotlin.math.roundToInt

public interface MagellanComposeTransition {
  @Composable
  public fun getTransitionForDirection(
    direction: Direction,
  ): AnimatedContentTransitionScope<Navigable<@Composable () -> Unit>?>.() -> ContentTransform
}

public class SimpleComposeTransition(
  public val transitionSpec: AnimatedContentTransitionScope<Navigable<@Composable () -> Unit>?>.(
    Direction,
  ) -> ContentTransform,
) : MagellanComposeTransition {
  @Composable
  public override fun getTransitionForDirection(
    direction: Direction,
  ): AnimatedContentTransitionScope<Navigable<@Composable () -> Unit>?>.() -> ContentTransform =
    {
      transitionSpec(direction)
    }
}

private const val SCALE_DOWN_FACTOR = 0.85f
private const val SCALE_UP_FACTOR = 1.15f

public val defaultTransition: MagellanComposeTransition =
  SimpleComposeTransition {
    when (it) {
      Direction.FORWARD -> {
        fadeIn() + scaleIn(initialScale = SCALE_DOWN_FACTOR) togetherWith
          fadeOut() + scaleOut(targetScale = SCALE_UP_FACTOR)
      }

      Direction.BACKWARD -> {
        fadeIn() + scaleIn(initialScale = SCALE_UP_FACTOR) togetherWith
          fadeOut() + scaleOut(targetScale = SCALE_DOWN_FACTOR)
      }
    }
  }

private const val HEIGHT_OFFSET_FACTOR = 0.8f

public val showTransition: MagellanComposeTransition =
  SimpleComposeTransition { direction ->
    when (direction) {
      Direction.FORWARD -> {
        fadeIn() +
          slideInVertically(
            initialOffsetY = { fullHeight -> (fullHeight * HEIGHT_OFFSET_FACTOR).roundToInt() },
          ) togetherWith
          fadeOut() + scaleOut(targetScale = SCALE_UP_FACTOR)
      }

      Direction.BACKWARD -> {
        fadeIn() + scaleIn(initialScale = SCALE_UP_FACTOR) togetherWith
          fadeOut() +
          slideOutVertically(
            targetOffsetY = { fullHeight -> (fullHeight * HEIGHT_OFFSET_FACTOR).roundToInt() },
          )
      }
    }
  }

public val noTransition: MagellanComposeTransition =
  SimpleComposeTransition {
    EnterTransition.None togetherWith ExitTransition.None
  }
