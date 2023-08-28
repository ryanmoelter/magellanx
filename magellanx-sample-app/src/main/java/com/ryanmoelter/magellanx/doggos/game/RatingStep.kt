package com.ryanmoelter.magellanx.doggos.game

import androidx.compose.runtime.Composable
import com.ryanmoelter.magellanx.compose.ComposeStep
import com.ryanmoelter.magellanx.doggos.injector
import com.ryanmoelter.magellanx.doggos.utils.Loadable
import kotlinx.coroutines.flow.MutableStateFlow

class RatingStep(
  val existingDoggoRating: DoggoRating? = null,
  val submitRating: (DoggoRating) -> Unit,
) : ComposeStep() {

  private val doggoApi = injector.doggoApi
  private val loadableImageUrlFlow: MutableStateFlow<Loadable<String>> =
    MutableStateFlow(Loadable.Loading())

  @Composable
  override fun Content() {
    TODO("Not yet implemented")
  }
}
