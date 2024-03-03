package com.ryanmoelter.magellanx.core.lifecycle

public interface LifecycleAware {
  public fun create() {}

  public fun show() {}

  public fun start() {}

  public fun resume() {}

  public fun pause() {}

  public fun stop() {}

  public fun hide() {}

  public fun destroy() {}
}
