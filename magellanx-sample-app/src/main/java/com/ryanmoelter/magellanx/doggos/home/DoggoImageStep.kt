package com.ryanmoelter.magellanx.doggos.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.ryanmoelter.magellanx.compose.ComposeStep
import com.ryanmoelter.magellanx.doggos.injector
import com.ryanmoelter.magellanx.doggos.utils.Loadable
import com.ryanmoelter.magellanx.doggos.utils.Loadable.Loading
import com.ryanmoelter.magellanx.doggos.utils.ShowLoadingAround
import com.ryanmoelter.magellanx.doggos.utils.map
import com.ryanmoelter.magellanx.doggos.utils.wrapInLoadableFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DoggoImageStep(
  val breed: String? = null
) : ComposeStep() {

  private val doggoApi = injector.doggoApi
  private val loadableImageUrlFlow: MutableStateFlow<Loadable<String>> =
    MutableStateFlow(Loading())

  @Composable
  override fun Content() {
    val loadableImageUrl by loadableImageUrlFlow.collectAsState()
    ShowLoadingAround(loadableImageUrl) { imageUrl ->
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        SubcomposeAsyncImage(
          model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
          contentDescription = "A good doggo",
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
          loading = {
            CircularProgressIndicator(modifier = Modifier.wrapContentSize())
          },
          error = {
            Row(
              modifier = Modifier.wrapContentSize(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Image failed to load",
                modifier = Modifier.size(32.dp)
              )
              Text(text = "Image failed to load")
            }
          }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { refresh() }) {
          Text("Another!")
        }
      }
    }
  }

  override fun onShow() {
    refresh()
  }

  private fun refresh() {
    shownScope.launch {
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
        .collect { loadableImageUrl ->
          loadableImageUrlFlow.value = loadableImageUrl
        }
    }
  }
}
