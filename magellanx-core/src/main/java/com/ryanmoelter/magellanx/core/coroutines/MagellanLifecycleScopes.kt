package com.ryanmoelter.magellanx.core.coroutines

import com.ryanmoelter.magellanx.core.init.Magellan
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleAware
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob

public open class MagellanLifecycleScope(
  private val lowercaseLifecycleDescription: String,
) : CoroutineScope, LifecycleAware {
  protected var job: Job =
    SupervisorJob().apply {
      cancel(CancellationException("Not $lowercaseLifecycleDescription yet"))
    }
    set(value) {
      field = value
      coroutineContext = value + Magellan.mainDispatcher
    }

  override var coroutineContext: CoroutineContext = job + Magellan.mainDispatcher
    protected set
}

public class CreatedLifecycleScope : MagellanLifecycleScope("created") {
  override fun create() {
    job = SupervisorJob()
  }

  override fun destroy(): Unit = job.cancel(CancellationException("destroyed"))
}

public class ShownLifecycleScope : MagellanLifecycleScope("shown") {
  override fun show() {
    job = SupervisorJob()
  }

  override fun hide(): Unit = job.cancel(CancellationException("Hidden"))
}

public class StartedLifecycleScope : MagellanLifecycleScope("started") {
  override fun start() {
    job = SupervisorJob()
  }

  override fun stop(): Unit = job.cancel(CancellationException("Stopped"))
}

public class ResumedLifecycleScope : MagellanLifecycleScope("resumed") {
  override fun resume() {
    job = SupervisorJob()
  }

  override fun pause(): Unit = job.cancel(CancellationException("Paused"))
}
