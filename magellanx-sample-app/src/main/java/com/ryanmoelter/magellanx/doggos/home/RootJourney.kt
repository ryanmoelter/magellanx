package com.ryanmoelter.magellanx.doggos.home

import com.ryanmoelter.magellanx.compose.ComposeJourney
import com.ryanmoelter.magellanx.doggos.Singleton
import com.ryanmoelter.magellanx.doggos.game.RatingGameJourney
import com.ryanmoelter.magellanx.doggos.randomimages.BreedImagesJourney
import com.ryanmoelter.magellanx.doggos.randomimages.DoggoImageStep
import com.ryanmoelter.magellanx.doggos.transitiontest.TransitionTestJourney
import me.tatarka.inject.annotations.Inject

@Inject
@Singleton
class RootJourney : ComposeJourney() {
  override fun onCreate() {
    super.onCreate()
    navigator.goTo(
      HomeStep(
        goToRandomDog = { navigator.goTo(DoggoImageStep()) },
        goToBreedList = { navigator.goTo(BreedImagesJourney()) },
        goToRatingGame = { navigator.goTo(RatingGameJourney(finish = { navigator.goBack() })) },
        goToTransitionTest = { navigator.goTo(TransitionTestJourney()) },
      ),
    )
  }
}
