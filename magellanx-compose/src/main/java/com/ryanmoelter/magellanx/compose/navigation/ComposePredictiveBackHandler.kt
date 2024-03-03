package com.ryanmoelter.magellanx.compose.navigation

import androidx.activity.BackEventCompat
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.runtime.Composable
import com.ryanmoelter.magellanx.compose.WhenStarted
import com.ryanmoelter.magellanx.core.Displayable
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleAwareComponent
import kotlinx.coroutines.flow.Flow

/**
 * Handle back events proactively, to work with Android's upcoming predictive back system.
 *
 * Note that you need to call [view] (or the Content(arg) extension function) in order for this to
 * work.
 *
 * @see [Android's upcoming predictive back system](https://developer.android.com/guide/navigation/custom-back/predictive-back-gesture)
 */
public class ComposePredictiveBackHandler(
  private val backStarted: suspend (Flow<BackEventCompat>) -> Unit,
) : LifecycleAwareComponent(), Displayable<@Composable (BackHandlerStatus) -> Unit> {
  override val view: @Composable (BackHandlerStatus) -> Unit
    get() = { backHandlerStatus ->
      WhenStarted {
        PredictiveBackHandler(enabled = backHandlerStatus.enabled, onBack = backStarted)
      }
    }
}

public enum class BackHandlerStatus(public val enabled: Boolean) {
  ENABLED(true),
  DISABLED(false),
}
