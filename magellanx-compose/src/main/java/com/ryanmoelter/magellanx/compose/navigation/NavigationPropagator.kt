package com.ryanmoelter.magellanx.compose.navigation

import com.ryanmoelter.magellanx.core.Navigable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

public object NavigationPropagator {
  internal fun onBeforeNavigation() {
    _beforeNavigation.tryEmit(Unit)
  }

  private val _beforeNavigation: MutableSharedFlow<Unit> = MutableSharedFlow()
  public val beforeNavigation: SharedFlow<Unit>
    get() = _beforeNavigation.asSharedFlow()

  internal fun onAfterNavigation() {
    _afterNavigation.tryEmit(Unit)
  }

  private val _afterNavigation: MutableSharedFlow<Unit> = MutableSharedFlow()
  public val afterNavigation: SharedFlow<Unit>
    get() = _afterNavigation.asSharedFlow()

  internal fun navigatedTo(navigable: Navigable<*>) {
    _onNavigatedTo.tryEmit(navigable)
  }

  private val _onNavigatedTo: MutableSharedFlow<Navigable<*>> = MutableSharedFlow()
  public val onNavigatedTo: SharedFlow<Navigable<*>>
    get() = _onNavigatedTo.asSharedFlow()

  internal fun navigatedFrom(navigable: Navigable<*>) {
    _onNavigatedFrom.tryEmit(navigable)
  }

  private val _onNavigatedFrom: MutableSharedFlow<Navigable<*>> = MutableSharedFlow()
  public val onNavigatedFrom: SharedFlow<Navigable<*>>
    get() = _onNavigatedFrom.asSharedFlow()
}
