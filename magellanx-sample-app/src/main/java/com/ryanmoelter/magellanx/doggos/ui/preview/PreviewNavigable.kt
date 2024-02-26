package com.ryanmoelter.magellanx.doggos.ui.preview

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ryanmoelter.magellanx.compose.Content
import com.ryanmoelter.magellanx.core.Displayable
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleAware
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleOwner
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState
import com.ryanmoelter.magellanx.core.lifecycle.transitionToState
import com.ryanmoelter.magellanx.doggos.FakeDoggoComponent
import com.ryanmoelter.magellanx.doggos.create
import com.ryanmoelter.magellanx.doggos.injector
import com.ryanmoelter.magellanx.doggos.ui.theme.DoggoTheme

/** Preview a given compose navigable, populating the theme and providing toaster scaffolding */
@Composable
fun <T> PreviewNavigable(
  createNavigable: () -> T,
) where T : Displayable<@Composable () -> Unit>, T : LifecycleAware, T : LifecycleOwner {
  val step =
    remember {
      @SuppressLint("VisibleForTests")
      injector = FakeDoggoComponent::class.create()
      val step = createNavigable().apply { transitionToState(LifecycleState.Resumed) }
      step
    }

  DoggoTheme {
    Surface(
      modifier = Modifier.fillMaxSize(),
      color = MaterialTheme.colorScheme.background,
    ) {
      Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        step.Content()
      }
    }
  }
}
