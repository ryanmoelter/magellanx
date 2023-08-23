package com.ryanmoelter.magellanx.doggos.randomimages

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ryanmoelter.magellanx.compose.ComposeStep
import com.ryanmoelter.magellanx.doggos.home.ListItem
import com.ryanmoelter.magellanx.doggos.injector
import com.ryanmoelter.magellanx.doggos.utils.Loadable
import com.ryanmoelter.magellanx.doggos.utils.ShowLoadingAround
import com.ryanmoelter.magellanx.doggos.utils.wrapInLoadableFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ChooseBreedStep(
  val chooseBreed: (String) -> Unit
) : ComposeStep() {

  val loadableBreedListFlow: MutableStateFlow<Loadable<List<String>>> =
    MutableStateFlow(Loadable.Loading())

  val doggoApi = injector.doggoApi

  @Composable
  override fun Content() {
    val loadableBreedList by loadableBreedListFlow.collectAsState()

    ShowLoadingAround(loadable = loadableBreedList) { breeds ->
      LazyColumn {
        items(breeds, key = { it }) { breed ->
          ListItem(title = breed, onClick = { chooseBreed(breed) })
        }
      }
    }
  }

  override fun onShow() {
    refresh()
  }

  fun refresh() {
    shownScope.launch {
      wrapInLoadableFlow {
        doggoApi.getBreeds().breeds
      }
        .collect { breeds -> loadableBreedListFlow.value = breeds }
    }
  }
}
