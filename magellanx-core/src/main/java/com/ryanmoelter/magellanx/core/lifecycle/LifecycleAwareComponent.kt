package com.ryanmoelter.magellanx.core.lifecycle

import kotlinx.coroutines.flow.StateFlow

public abstract class LifecycleAwareComponent :
  LifecycleAware,
  LifecycleOwner {
  protected val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry()

  override val children: List<LifecycleAware>
    get() = lifecycleRegistry.listeners.toList()

  override val currentStateFlow: StateFlow<LifecycleState>
    get() = lifecycleRegistry.currentStateFlow

  final override fun create() {
    if (currentState != LifecycleState.Destroyed) {
      throw IllegalStateException(
        "Cannot create() from a state that is not Destroyed: " +
          "${this::class.java.simpleName} is ${currentState.name}",
      )
    }
    lifecycleRegistry.create()
    onCreate()
  }

  final override fun show() {
    if (currentState != LifecycleState.Created) {
      throw IllegalStateException(
        "Cannot show() from a state that is not Created: " +
          "${this::class.java.simpleName} is ${currentState.name}",
      )
    }
    lifecycleRegistry.show()
    onShow()
  }

  final override fun start() {
    if (currentState != LifecycleState.Shown) {
      throw IllegalStateException(
        "Cannot start() from a state that is not Shown: " +
          "${this::class.java.simpleName} is ${currentState.name}",
      )
    }
    lifecycleRegistry.start()
    onStart()
  }

  final override fun resume() {
    if (currentState != LifecycleState.Started) {
      throw IllegalStateException(
        "Cannot resume() from a state that is not Started: " +
          "${this::class.java.simpleName} is ${currentState.name}",
      )
    }
    lifecycleRegistry.resume()
    onResume()
  }

  final override fun pause() {
    if (currentState != LifecycleState.Resumed) {
      throw IllegalStateException(
        "Cannot pause() from a state that is not Resumed: " +
          "${this::class.java.simpleName} is ${currentState.name}",
      )
    }
    onPause()
    lifecycleRegistry.pause()
  }

  final override fun stop() {
    if (currentState != LifecycleState.Started) {
      throw IllegalStateException(
        "Cannot stop() from a state that is not Started: " +
          "${this::class.java.simpleName} is ${currentState.name}",
      )
    }
    onStop()
    lifecycleRegistry.stop()
  }

  final override fun hide() {
    if (currentState != LifecycleState.Shown) {
      throw IllegalStateException(
        "Cannot hide() from a state that is not Shown: " +
          "${this::class.java.simpleName} is ${currentState.name}",
      )
    }
    onHide()
    lifecycleRegistry.hide()
  }

  final override fun destroy() {
    if (currentState != LifecycleState.Created) {
      throw IllegalStateException(
        "Cannot destroy() from a state that is not Created: " +
          "${this::class.java.simpleName} is ${currentState.name}",
      )
    }
    onDestroy()
    lifecycleRegistry.destroy()
  }

  override fun backPressed(): Boolean = lifecycleRegistry.backPressed() || onBackPressed()

  public fun attachToLifecycle(lifecycleAware: LifecycleAware) {
    attachToLifecycle(lifecycleAware, LifecycleState.Destroyed)
  }

  public fun removeFromLifecycle(lifecycleAware: LifecycleAware) {
    removeFromLifecycle(lifecycleAware, LifecycleState.Destroyed)
  }

  override fun attachToLifecycle(
    lifecycleAware: LifecycleAware,
    detachedState: LifecycleState,
  ) {
    lifecycleRegistry.attachToLifecycle(lifecycleAware, detachedState)
  }

  override fun removeFromLifecycle(
    lifecycleAware: LifecycleAware,
    detachedState: LifecycleState,
  ) {
    lifecycleRegistry.removeFromLifecycle(lifecycleAware, detachedState)
  }

  protected open fun onCreate() {}

  protected open fun onShow() {}

  protected open fun onStart() {}

  protected open fun onResume() {}

  protected open fun onPause() {}

  protected open fun onStop() {}

  protected open fun onHide() {}

  protected open fun onDestroy() {}

  protected open fun onBackPressed(): Boolean = false
}
