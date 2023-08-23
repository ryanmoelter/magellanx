package com.ryanmoelter.magellanx.doggos

import kotlinx.serialization.Serializable
import retrofit2.http.GET

interface DoggoApi {
  @GET("breeds/image/random")
  suspend fun getRandomDoggoImage(): DoggoImageResponse
}

@Serializable
data class DoggoImageResponse(
  val message: String,
  val status: String
)
