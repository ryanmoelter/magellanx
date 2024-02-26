package com.ryanmoelter.magellanx.doggos

import android.app.Application
import org.jetbrains.annotations.VisibleForTesting

class DoggoApp : Application() {
  override fun onCreate() {
    super.onCreate()
    injector = RealDoggoComponent::class.create()
  }
}

lateinit var injector: DoggoComponent
  @VisibleForTesting set
