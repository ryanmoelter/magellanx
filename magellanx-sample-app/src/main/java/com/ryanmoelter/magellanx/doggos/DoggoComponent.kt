package com.ryanmoelter.magellanx.doggos

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ryanmoelter.magellanx.doggos.home.RootJourney
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
annotation class ApplicationScope

@Component
@ApplicationScope
abstract class DoggoComponent(
  val applicationContext: Context
) {

  abstract val rootJourney: RootJourney

  val retrofit: Retrofit
    @Provides @ApplicationScope get() = Retrofit.Builder()
      .baseUrl("https://dog.ceo/api/")
      .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
      .build()

  @Provides
  fun provideDoggoApi(retrofit: Retrofit): DoggoApi = retrofit.create(DoggoApi::class.java)

  @Provides
  fun provideApplicationContext(): Context = applicationContext
}
