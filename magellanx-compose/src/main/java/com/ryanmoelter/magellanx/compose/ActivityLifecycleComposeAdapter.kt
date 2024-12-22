package com.ryanmoelter.magellanx.compose

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.DefaultLifecycleObserver
import com.ryanmoelter.magellanx.core.Navigable
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleOwner
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Destroyed
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleState.Shown
import com.ryanmoelter.magellanx.core.lifecycle.transition

private typealias ActivityLifecycleOwner = androidx.lifecycle.LifecycleOwner
private typealias ActivityLifecycle = androidx.lifecycle.Lifecycle
private typealias AdapterLifecyclePair = Pair<ActivityLifecycleComposeAdapter, ActivityLifecycle>

private var adapterMap = emptyMap<Navigable<@Composable () -> Unit>, AdapterLifecyclePair>()

public class ActivityLifecycleComposeAdapter(
  private val navigable: Navigable<@Composable () -> Unit>,
  private val context: Activity,
) : DefaultLifecycleObserver {
  override fun onStart(owner: ActivityLifecycleOwner) {
    navigable.start()
  }

  override fun onResume(owner: ActivityLifecycleOwner) {
    navigable.resume()
  }

  override fun onPause(owner: ActivityLifecycleOwner) {
    navigable.pause()
  }

  override fun onStop(owner: ActivityLifecycleOwner) {
    navigable.stop()
  }

  override fun onDestroy(owner: ActivityLifecycleOwner) {
    if (context.isFinishing) {
      navigable.hide()
      navigable.destroy()
    }
  }
}

/**
 * Attach [navigable] as the root [Navigable] for this Activity. Sets the UI, attaches the
 * lifecycle, and handles back navigation. Should be called in [Activity.onCreate].
 */
public fun ComponentActivity.setContentNavigable(navigable: Navigable<@Composable () -> Unit>) {
  if (navigable is LifecycleOwner && navigable.currentState == Destroyed) {
    navigable.create()
    navigable.show()
  }
  if (adapterMap.containsKey(navigable)) {
    navigable.detachAndRemoveFromStaticMap()
  }
  val lifecycleAdapter = ActivityLifecycleComposeAdapter(navigable, this)
  navigable.attachAndAddToStaticMap(lifecycleAdapter, lifecycle)

  setContent { navigable.Content() }

  onBackPressedDispatcher.addCallback {
    if (!navigable.backPressed()) {
      this@setContentNavigable.finish()
    }
  }
}

private fun Navigable<@Composable () -> Unit>.attachAndAddToStaticMap(
  lifecycleAdapter: ActivityLifecycleComposeAdapter,
  lifecycle: ActivityLifecycle,
) {
  lifecycle.addObserver(lifecycleAdapter)
  adapterMap = adapterMap + (this to (lifecycleAdapter to lifecycle))
}

private fun Navigable<@Composable () -> Unit>.detachAndRemoveFromStaticMap() {
  val (lifecycleAdapter, lifecycle) = adapterMap[this]!!
  lifecycle.removeObserver(lifecycleAdapter)
  if (this is LifecycleOwner && currentState >= Shown) {
    transition(this.currentState, Shown)
  }
  adapterMap = adapterMap - this
}
