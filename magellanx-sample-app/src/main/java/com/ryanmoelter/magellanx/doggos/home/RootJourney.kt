package com.ryanmoelter.magellanx.doggos.home

import com.ryanmoelter.magellanx.compose.ComposeJourney
import com.ryanmoelter.magellanx.doggos.ApplicationScope
import me.tatarka.inject.annotations.Inject

@Inject
@ApplicationScope
class RootJourney : ComposeJourney() {

  override fun onCreate() {
    super.onCreate()
    navigator.goTo(
      HomeStep(
        goToRandomDog = { navigator.goTo(DoggoImageStep()) },
        goToBreedList = { navigator.goTo(BreedImagesJourney()) },
      )
    )
  }
}
