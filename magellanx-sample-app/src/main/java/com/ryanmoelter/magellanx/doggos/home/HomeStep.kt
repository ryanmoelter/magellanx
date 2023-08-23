package com.ryanmoelter.magellanx.doggos.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ryanmoelter.magellanx.compose.ComposeStep
import com.ryanmoelter.magellanx.doggos.ui.theme.DoggoTheme
import me.tatarka.inject.annotations.Inject

@Inject
class HomeStep(
  val goToRandomDog: () -> Unit,
  val goToBreedList: () -> Unit,
) : ComposeStep() {

  @Composable
  override fun Content() = Home(goToRandomDog, goToBreedList)
}

@Composable
fun Home(
  goToRandomDog: () -> Unit,
  goToBreedList: () -> Unit,
) {
  Column {
    ListItem(title = "Random doggo") { goToRandomDog() }
    ListItem(title = "Breed list") { goToBreedList() }
  }
}

@Composable
fun ListItem(
  title: String,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .clickable { onClick() }
      .padding(horizontal = 24.dp, vertical = 12.dp)
      .fillMaxWidth()
  ) {
    Text(title)
  }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
  DoggoTheme {
    Home(goToRandomDog = { }, goToBreedList = { })
  }
}
