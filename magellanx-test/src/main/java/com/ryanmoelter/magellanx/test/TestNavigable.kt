package com.ryanmoelter.magellanx.test

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ryanmoelter.magellanx.core.Navigable
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleAwareComponent
import com.ryanmoelter.magellanx.core.lifecycle.createAndAttachFieldToLifecycleWhenStarted

/**
 * A simple [Navigable] implementation for use in tests.
 */
public class TestNavigable : Navigable<@Composable () -> Unit>, LifecycleAwareComponent() {
  public override val view: (@Composable () -> Unit)? by
    createAndAttachFieldToLifecycleWhenStarted {
      {
        Box(Modifier)
      }
    }
}
