package com.ryanmoelter.magellanx.doggos.transitiontest

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.ryanmoelter.magellanx.compose.ComposeJourney
import com.ryanmoelter.magellanx.compose.ComposeStep
import com.ryanmoelter.magellanx.compose.navigation.Direction
import com.ryanmoelter.magellanx.compose.transitions.SimpleComposeTransition
import com.ryanmoelter.magellanx.doggos.ui.preview.PreviewNavigable

private const val SCALE_DOWN_FACTOR = 0.85f
private const val SCALE_UP_FACTOR = 1.15f

class TransitionTestJourney : ComposeJourney() {
  override fun onCreate() {
    next()
  }

  private fun next() {
    navigator.goTo(
      TransitionTestStep { next() },
      overrideTransitionSpec =
        SimpleComposeTransition {
          when (it) {
            Direction.FORWARD -> {
              fadeIn(spring(stiffness = Spring.StiffnessVeryLow)) +
                scaleIn(
                  initialScale = SCALE_DOWN_FACTOR,
                  animationSpec = spring(stiffness = Spring.StiffnessVeryLow),
                ) togetherWith
                fadeOut(spring(stiffness = Spring.StiffnessVeryLow)) +
                scaleOut(
                  targetScale = SCALE_UP_FACTOR,
                  animationSpec = spring(stiffness = Spring.StiffnessVeryLow),
                )
            }

            Direction.BACKWARD -> {
              fadeIn(spring(stiffness = Spring.StiffnessVeryLow)) +
                scaleIn(
                  initialScale = SCALE_UP_FACTOR,
                  animationSpec = spring(stiffness = Spring.StiffnessVeryLow),
                ) togetherWith
                fadeOut(spring(stiffness = Spring.StiffnessVeryLow)) +
                scaleOut(
                  targetScale = SCALE_DOWN_FACTOR,
                  animationSpec = spring(stiffness = Spring.StiffnessVeryLow),
                )
            }
          }
        },
    )
  }
}

class TransitionTestStep(
  val next: () -> Unit,
) : ComposeStep() {
  val resumedFlow = lifecycleRegistry.currentStateFlow

  @Composable
  override fun Content() {
    val currentState by resumedFlow.collectAsState()
    val currentStateWhileResumed by resumedFlow.collectAsStateWhileResumed()
    Column {
      Text("Current state: $currentState")
      Text("Current state while resumed: $currentStateWhileResumed")
      Button(onClick = { next() }) {
        Text("Next")
      }
    }
  }
}

@Preview
@Composable
private fun TransitionTestStep_Preview() =
  PreviewNavigable {
    TransitionTestJourney()
  }
