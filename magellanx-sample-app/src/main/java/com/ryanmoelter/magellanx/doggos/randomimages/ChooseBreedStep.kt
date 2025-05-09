package com.ryanmoelter.magellanx.doggos.randomimages

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ryanmoelter.magellanx.compose.ComposeStep
import com.ryanmoelter.magellanx.doggos.home.ListItem
import com.ryanmoelter.magellanx.doggos.injector
import com.ryanmoelter.magellanx.doggos.utils.Loadable
import com.ryanmoelter.magellanx.doggos.utils.ShowLoadingAround
import com.ryanmoelter.magellanx.doggos.utils.wrapInLoadableFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ChooseBreedStep(
  val chooseBreed: (String) -> Unit,
) : ComposeStep() {
  val loadableBreedListFlow: MutableStateFlow<Loadable<List<String>>> =
    MutableStateFlow(Loadable.Loading())

  val doggoApi = injector.doggoApi

  @Composable
  override fun Content() {
    val loadableBreedList by loadableBreedListFlow.collectAsState()

    ShowLoadingAround(loadable = loadableBreedList) { breeds ->
      LazyColumn {
        item { Spacer(modifier = Modifier.height(12.dp)) }
        items(breeds, key = { it }) { breed ->
          ListItem(
            title = breed.replaceFirstChar { it.uppercase() },
            onClick = { chooseBreed(breed) },
          )
        }
        item { Spacer(modifier = Modifier.height(12.dp)) }
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
      }.collect { breeds -> loadableBreedListFlow.value = breeds }
    }
  }
}
