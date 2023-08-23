package com.ryanmoelter.magellanx.doggos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path

interface DoggoApi {

  @GET("breeds/image/random")
  suspend fun getRandomDoggoImage(): DoggoImageResponse

  @GET("breed/{breed}/images/random")
  suspend fun getRandomDoggoImageByBreed(@Path("breed") breed: String): DoggoImageResponse

  @GET("breeds/list")
  suspend fun getBreeds(): BreedListResponse
}

@Serializable
data class DoggoImageResponse(
  @SerialName("message")
  val imageUrl: String,
  val status: String
)

@Serializable
data class BreedListResponse(
  @SerialName("message")
  val breeds: List<String>,
  val status: String
)
