package com.ryanmoelter.magellanx.core.init

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

public object Magellan {
  // TODO: re-enable these
  // internal var logDebugInfo: Boolean = false
  // internal var disableAnimations: Boolean = false
  internal var overrideMainDispatcher: CoroutineDispatcher? = null

  @JvmStatic
  @JvmOverloads
  public fun init(
    // disableAnimations: Boolean = false,
    // logDebugInfo: Boolean = false,
    mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
  ) {
    // this.logDebugInfo = logDebugInfo
    // this.disableAnimations = disableAnimations
    this.overrideMainDispatcher = mainDispatcher
  }
}

// internal fun shouldRunAnimations(): Boolean = !disableAnimations
//
// internal fun shouldLogDebugInfo(): Boolean = logDebugInfo
