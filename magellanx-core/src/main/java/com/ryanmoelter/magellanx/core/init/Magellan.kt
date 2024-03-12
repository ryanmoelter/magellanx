package com.ryanmoelter.magellanx.core.init

import com.ryanmoelter.magellanx.core.init.Magellan.disableAnimations
import com.ryanmoelter.magellanx.core.init.Magellan.logDebugInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

public object Magellan {
  internal var logDebugInfo: Boolean = false
  internal var disableAnimations: Boolean = false
  internal var mainDispatcher: CoroutineDispatcher = Dispatchers.Main

  @JvmStatic
  @JvmOverloads
  public fun init(
    disableAnimations: Boolean = false,
    logDebugInfo: Boolean = false,
    mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
  ) {
    this.logDebugInfo = logDebugInfo
    this.disableAnimations = disableAnimations
    this.mainDispatcher = mainDispatcher
  }
}

internal fun shouldRunAnimations(): Boolean = !disableAnimations

internal fun shouldLogDebugInfo(): Boolean = logDebugInfo
