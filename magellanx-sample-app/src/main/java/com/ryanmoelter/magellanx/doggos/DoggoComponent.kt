package com.ryanmoelter.magellanx.doggos

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ryanmoelter.magellanx.doggos.home.RootJourney
import com.ryanmoelter.magellanx.doggos.randomimages.RandomDoggoImageUrlGetter
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import me.tatarka.inject.annotations.Scope
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

@Scope
@Target(CLASS, FUNCTION, PROPERTY_GETTER)
annotation class Singleton

interface DoggoComponent {
  val rootJourney: RootJourney
  val doggoApi: DoggoApi
  val randomDoggoImageUrlGetter: RandomDoggoImageUrlGetter
}

@Component
@Singleton
abstract class RealDoggoComponent : DoggoComponent {
  val retrofit: Retrofit
    @Provides @Singleton
    get() =
      Retrofit.Builder()
        .baseUrl("https://dog.ceo/api/")
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()

  @Provides
  fun provideDoggoApi(retrofit: Retrofit): DoggoApi = retrofit.create(DoggoApi::class.java)
}

@Component
@Singleton
abstract class FakeDoggoComponent : DoggoComponent {
  @Provides
  fun provideDoggoApi(): DoggoApi = FakeDoggoApi()
}
