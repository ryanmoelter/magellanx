package com.ryanmoelter.magellanx.doggos.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ryanmoelter.magellanx.compose.ComposeStep
import com.ryanmoelter.magellanx.doggos.ui.theme.DoggoTheme

class HomeStep(
  val goToRandomDog: () -> Unit,
  val goToBreedList: () -> Unit,
  val goToRatingGame: () -> Unit,
) : ComposeStep() {

  @Composable
  override fun Content() = Home(goToRandomDog, goToBreedList, goToRatingGame)
}

@Composable
fun Home(
  goToRandomDog: () -> Unit,
  goToBreedList: () -> Unit,
  goToRatingGame: () -> Unit,
) {
  Column {
    Text(
      text = "Magellan Doggos",
      style = MaterialTheme.typography.displayMedium,
      modifier = Modifier
        .padding(horizontal = 24.dp, vertical = 12.dp)
    )
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp)
        .height(1.dp)
        .background(color = MaterialTheme.colorScheme.outline)
    )
    ListItem(title = "Show random doggo") { goToRandomDog() }
    ListItem(title = "Filter by breed list") { goToBreedList() }
    ListItem(title = "Rate doggos") { goToRatingGame() }
    Spacer(modifier = Modifier.height(24.dp))
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
      .minimumInteractiveComponentSize()
      .padding(horizontal = 24.dp, vertical = 12.dp)
      .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
    Icon(
      imageVector = Icons.Rounded.ArrowForward,
      contentDescription = "Open a new page",
      tint = MaterialTheme.colorScheme.primary,
    )
  }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
  DoggoTheme {
    Home(goToRandomDog = { }, goToBreedList = { }, goToRatingGame = { })
  }
}
