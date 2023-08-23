package com.ryanmoelter.magellanx.doggos.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.LocalImageLoader
import coil.request.ImageRequest
import com.ryanmoelter.magellanx.compose.ComposeStep
import com.ryanmoelter.magellanx.doggos.injector
import com.ryanmoelter.magellanx.doggos.utils.Loadable
import com.ryanmoelter.magellanx.doggos.utils.Loadable.Failure
import com.ryanmoelter.magellanx.doggos.utils.Loadable.Loading
import com.ryanmoelter.magellanx.doggos.utils.Loadable.Success
import com.ryanmoelter.magellanx.doggos.utils.map
import com.ryanmoelter.magellanx.doggos.utils.wrapInLoadableFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DoggoImageStep : ComposeStep() {

  private val doggoApi = injector.doggoApi
  private val loadableImageUrlFlow: MutableStateFlow<Loadable<String>> =
    MutableStateFlow(Loading())

  @Composable
  override fun Content() {
    val loadableImageUrl by loadableImageUrlFlow.collectAsState()
    AnimatedContent(targetState = loadableImageUrl, label = "") { loadable ->
      when (loadable) {
        is Failure -> Text(text = loadable.throwable.toString())
        is Loading -> CircularProgressIndicator()
        is Success -> {
          AsyncImage(
            model =
            ImageRequest.Builder(LocalContext.current)
              .data(loadable.value)
              .crossfade(true)
              .build(),
            contentDescription = "A good doggo",
            modifier = Modifier.fillMaxSize()
          )
        }
      }
    }
  }

  override fun onShow() {
    shownScope.launch {
      wrapInLoadableFlow { doggoApi.getRandomDoggoImage() }
        .map { loadable ->
          loadable.map { doggoImageResponse -> doggoImageResponse.message }
        }
        .collect { loadableImageUrl ->
          loadableImageUrlFlow.value = loadableImageUrl
        }
    }
  }
}
