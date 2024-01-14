package com.ryanmoelter.magellanx.compose.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.ryanmoelter.magellanx.compose.WhenShown
import com.ryanmoelter.magellanx.compose.navigation.BackstackStatus.*
import com.ryanmoelter.magellanx.core.Displayable
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleAwareComponent

/**
 * Handle back events proactively, to work with Android's upcoming predictive back system.
 *
 * Note that you need to call [view] (or the Content(arg) extension function) in order for this to
 * work.
 *
 * @see [Android's upcoming predictive back system](https://developer.android.com/guide/navigation/custom-back/predictive-back-gesture)
 */
public class ComposePredictiveBackHandler(
  private val onBackTriggered: () -> Unit
) : LifecycleAwareComponent(), Displayable<@Composable (BackstackStatus) -> Unit> {

  override val view: @Composable (BackstackStatus) -> Unit
    get() = { backstackStatus ->
      WhenShown {
        val enabled = when (backstackStatus) {
          AT_ROOT -> false
          BACK_AVAILABLE -> true
        }
        BackHandler(enabled = enabled) {
          onBackTriggered()
        }
      }
    }
}

public enum class BackstackStatus {
  AT_ROOT, BACK_AVAILABLE
}
