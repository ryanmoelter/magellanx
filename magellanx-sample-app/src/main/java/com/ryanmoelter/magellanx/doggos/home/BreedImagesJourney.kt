package com.ryanmoelter.magellanx.doggos.home

import com.ryanmoelter.magellanx.compose.ComposeJourney

class BreedImagesJourney : ComposeJourney() {

  override fun onCreate() {
    navigator.goTo(
      ChooseBreedStep(chooseBreed = { breed -> navigator.goTo(DoggoImageStep(breed)) })
    )
  }
}

