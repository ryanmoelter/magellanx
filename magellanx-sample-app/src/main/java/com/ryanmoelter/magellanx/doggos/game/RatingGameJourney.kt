package com.ryanmoelter.magellanx.doggos.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ryanmoelter.magellanx.compose.ComposeJourney
import com.ryanmoelter.magellanx.doggos.DoggoApi
import com.ryanmoelter.magellanx.doggos.injector
import com.ryanmoelter.magellanx.doggos.utils.Loadable
import com.ryanmoelter.magellanx.doggos.utils.ShowLoadingAround
import com.ryanmoelter.magellanx.doggos.utils.map
import com.ryanmoelter.magellanx.doggos.utils.wrapInLoadableFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

const val NUM_RATINGS_IN_GAME = 10

class RatingGameJourney(
  val finish: () -> Unit,
) : ComposeJourney() {
  private var doggoRatings: List<DoggoRating> = emptyList()
  private val doggoApi: DoggoApi = injector.doggoApi

  private val isLoadingFlow = MutableStateFlow(true)

  @Composable
  override fun Content() {
    val isLoading by isLoadingFlow.collectAsState()
    ShowLoadingAround(isLoading = isLoading, showSpinner = false) {
      super.Content()
    }
  }

  override fun onCreate() {
    createdScope.launch { getImageAndGoToNextStep() }
  }

  private suspend fun getImageAndGoToNextStep() {
    wrapInLoadableFlow { doggoApi.getRandomDoggoImage() }
      .map { loadableResponse ->
        loadableResponse.map { it.imageUrl }
      }
      .collect { loadableString ->
        when (loadableString) {
          is Loadable.Failure -> TODO()
          is Loadable.Loading -> {
            isLoadingFlow.value = true
          }

          is Loadable.Success -> {
            isLoadingFlow.value = false
            navigator.goTo(
              RatingStep(
                loadableString.value,
                submitRating = ::rateDoggoAndGoToNext,
              ),
            )
          }
        }
      }
  }

  private fun rateDoggoAndGoToNext(doggoRating: DoggoRating) {
    doggoRatings = doggoRatings + doggoRating
    if (doggoRatings.size < NUM_RATINGS_IN_GAME) {
      createdScope.launch { getImageAndGoToNextStep() }
    } else {
      navigator.goTo(ResultsStep(doggoRatings, goHome = { finish() }))
    }
  }

  override fun onBackPressed(): Boolean =
    super.onBackPressed().also { backHandled ->
      if (backHandled) {
        doggoRatings = doggoRatings.dropLast(1)
      }
    }
}

data class DoggoRating(
  val doggoImageUrl: String,
  val liked: Boolean,
)
