package com.ryanmoelter.magellanx.doggos.randomimages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ryanmoelter.magellanx.compose.ComposeStep
import com.ryanmoelter.magellanx.doggos.injector
import com.ryanmoelter.magellanx.doggos.ui.components.AsyncDoggoImage
import com.ryanmoelter.magellanx.doggos.utils.Loadable
import com.ryanmoelter.magellanx.doggos.utils.Loadable.Loading
import com.ryanmoelter.magellanx.doggos.utils.ShowLoadingAround
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DoggoImageStep(
  val breed: String? = null,
) : ComposeStep() {
  private val randomDoggoImageUrlGetter = injector.randomDoggoImageUrlGetter
  private val loadableImageUrlFlow: MutableStateFlow<Loadable<String>> =
    MutableStateFlow(Loading())

  @Composable
  override fun Content() {
    val loadableImageUrl by loadableImageUrlFlow.collectAsState()
    ShowLoadingAround(loadableImageUrl) { imageUrl ->
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncDoggoImage(
          imageUrl,
          modifier =
            Modifier
              .fillMaxWidth()
              .weight(1f),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { refresh() }) {
          Text("Another!")
        }
        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }

  override fun onCreate() {
    refresh()
  }

  private fun refresh() {
    createdScope.launch {
      randomDoggoImageUrlGetter
        .fetchDoggoImageUrl(breed)
        .collect { loadableImageUrl ->
          loadableImageUrlFlow.value = loadableImageUrl
        }
    }
  }
}
