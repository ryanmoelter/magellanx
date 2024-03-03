package com.ryanmoelter.magellanx.core.lifecycle

import com.ryanmoelter.magellanx.core.lifecycle.LifecycleLimit.NO_LIMIT
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleLimit.SHOWN
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

public class LifecycleRegistry : LifecycleAware, LifecycleOwner {
  internal val listeners: Set<LifecycleAware>
    get() = listenersToMaxStates.keys
  private var listenersToMaxStates: Map<LifecycleAware, LifecycleLimit> = linkedMapOf()

  override val children: List<LifecycleAware>
    get() = listeners.toList()

  private val _currentStateFlow: MutableStateFlow<LifecycleState> =
    MutableStateFlow(LifecycleState.Destroyed)
  override val currentStateFlow: StateFlow<LifecycleState> get() = _currentStateFlow.asStateFlow()

  override var currentState: LifecycleState
    get() = currentStateFlow.value
    private set(newState) {
      val oldState = currentStateFlow.value
      listenersToMaxStates.forEach { (lifecycleAware, maxState) ->
        if (oldState.limitBy(maxState) != newState.limitBy(maxState)) {
          lifecycleAware.transition(
            oldState.limitBy(maxState),
            newState.limitBy(maxState),
          )
        }
      }
      _currentStateFlow.value = newState
    }

  override fun attachToLifecycle(
    lifecycleAware: LifecycleAware,
    detachedState: LifecycleState,
  ) {
    attachToLifecycleWithMaxState(lifecycleAware, NO_LIMIT, detachedState)
  }

  public fun attachToLifecycleWithMaxState(
    lifecycleAware: LifecycleAware,
    maxState: LifecycleLimit = NO_LIMIT,
    detachedState: LifecycleState = LifecycleState.Destroyed,
  ) {
    if (listenersToMaxStates.containsKey(lifecycleAware)) {
      throw IllegalStateException(
        "Cannot attach a lifecycleAware that is already a child: " +
          lifecycleAware::class.java.simpleName,
      )
    }
    lifecycleAware.transition(detachedState, currentState.limitBy(maxState))
    listenersToMaxStates = listenersToMaxStates + (lifecycleAware to maxState)
  }

  override fun removeFromLifecycle(
    lifecycleAware: LifecycleAware,
    detachedState: LifecycleState,
  ) {
    if (!listenersToMaxStates.containsKey(lifecycleAware)) {
      throw IllegalStateException(
        "Cannot remove a lifecycleAware that is not a child: " +
          lifecycleAware::class.java.simpleName,
      )
    }
    val maxState = listenersToMaxStates[lifecycleAware]!!
    listenersToMaxStates = listenersToMaxStates - lifecycleAware
    lifecycleAware.transition(currentState.limitBy(maxState), detachedState)
  }

  public fun updateMaxState(
    lifecycleAware: LifecycleAware,
    maxState: LifecycleLimit,
  ) {
    if (!listenersToMaxStates.containsKey(lifecycleAware)) {
      throw IllegalArgumentException(
        "Cannot update the state of a lifecycleAware that is not a child: " +
          lifecycleAware::class.java.simpleName,
      )
    }
    val oldMaxState = listenersToMaxStates[lifecycleAware]!!
    if (oldMaxState != maxState) {
      val needsToTransition = !currentState.isWithinLimit(minOf(maxState, oldMaxState))
      if (needsToTransition) {
        lifecycleAware.transition(
          currentState.limitBy(oldMaxState),
          currentState.limitBy(maxState),
        )
      }
      listenersToMaxStates = listenersToMaxStates + (lifecycleAware to maxState)
    }
  }

  override fun create() {
    currentState = LifecycleState.Created
  }

  override fun show() {
    currentState = LifecycleState.Shown
  }

  override fun start() {
    currentState = LifecycleState.Started
  }

  override fun resume() {
    currentState = LifecycleState.Resumed
  }

  override fun pause() {
    currentState = LifecycleState.Started
  }

  override fun stop() {
    currentState = LifecycleState.Shown
  }

  override fun hide() {
    currentState = LifecycleState.Created
  }

  override fun destroy() {
    currentState = LifecycleState.Destroyed
  }

  override fun backPressed(): Boolean = onAllActiveListenersUntilTrue { it.backPressed() }

  private fun onAllActiveListenersUntilTrue(action: (LifecycleAware) -> Boolean): Boolean =
    listenersToMaxStates
      .asSequence()
      .filter { it.value >= SHOWN }
      .map { it.key }
      .map(action)
      .any { it }
}

public enum class LifecycleLimit(internal val maxLifecycleState: LifecycleState) {
  DESTROYED(LifecycleState.Destroyed),
  CREATED(LifecycleState.Created),
  SHOWN(LifecycleState.Shown),
  STARTED(LifecycleState.Started),
  NO_LIMIT(LifecycleState.Resumed),
}

private fun LifecycleState.isWithinLimit(limit: LifecycleLimit): Boolean =
  this <= limit.maxLifecycleState

private fun LifecycleState.limitBy(limit: LifecycleLimit): LifecycleState =
  minOf(this, limit.maxLifecycleState)
