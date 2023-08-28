package com.ryanmoelter.magellanx.doggos.randomimages

import com.ryanmoelter.magellanx.doggos.DoggoApi
import com.ryanmoelter.magellanx.doggos.utils.Loadable
import com.ryanmoelter.magellanx.doggos.utils.map
import com.ryanmoelter.magellanx.doggos.utils.wrapInLoadableFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@Inject
class RandomDoggoImageUrlGetter(
  private val doggoApi: DoggoApi
) {

  suspend fun fetchDoggoImageUrl(breed: String? = null): Flow<Loadable<String>> =
    wrapInLoadableFlow {
      if (breed == null) {
        doggoApi.getRandomDoggoImage()
      } else {
        doggoApi.getRandomDoggoImageByBreed(breed)
      }
    }
      .map { loadable ->
        loadable.map { doggoImageResponse -> doggoImageResponse.imageUrl }
      }
}
