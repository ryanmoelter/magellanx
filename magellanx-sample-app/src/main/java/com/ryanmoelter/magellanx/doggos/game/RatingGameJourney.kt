package com.ryanmoelter.magellanx.doggos.game

import androidx.annotation.IntRange
import com.ryanmoelter.magellanx.compose.ComposeJourney

const val NUM_RATINGS_IN_GAME = 10

class RatingGameJourney : ComposeJourney() {

  var doggoRatings: List<DoggoRating> = emptyList()

  override fun onCreate() {
    navigator.goTo(RatingStep(submitRating = ::rateDoggoAndGoToNext))
  }

  private fun rateDoggoAndGoToNext(doggoRating: DoggoRating) {
    doggoRatings = doggoRatings + doggoRating
    if (doggoRatings.size < NUM_RATINGS_IN_GAME) {
      navigator.goTo(RatingStep(submitRating = ::rateDoggoAndGoToNext))
    } else {
      TODO("Go to review page")
    }
  }

  override fun onBackPressed(): Boolean = super.onBackPressed().also { backHandled ->
    if (backHandled) {
      doggoRatings = doggoRatings.dropLast(1)
    }
  }
}

data class DoggoRating(
  val doggoImageUrl: String,
  @IntRange(from = 0, to = 10) val rating: Int
)
