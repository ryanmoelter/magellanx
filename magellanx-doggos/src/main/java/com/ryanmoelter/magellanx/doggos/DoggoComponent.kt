package com.ryanmoelter.magellanx.doggos

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

@Component
abstract class DoggoComponent {

  val retrofit: Retrofit
    @Provides get() = Retrofit.Builder()
      .baseUrl("https://dog.ceo/api/")
      .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
      .build()

  @Provides
  fun provideDoggoApi(retrofit: Retrofit): DoggoApi = retrofit.create(DoggoApi::class.java)
}
