package com.ryanmoelter.magellanx.doggos.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ryanmoelter.magellanx.compose.ComposeStep
import com.ryanmoelter.magellanx.doggos.ui.components.AsyncDoggoImage
import com.ryanmoelter.magellanx.doggos.ui.preview.PreviewNavigable

class RatingStep(
  private val doggoImageUrl: String,
  private val submitRating: (DoggoRating) -> Unit,
) : ComposeStep() {
  @Composable
  override fun Content() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      AsyncDoggoImage(
        doggoImageUrl,
        modifier =
          Modifier
            .fillMaxWidth()
            .weight(1f),
      )
      Row(
        modifier =
          Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Button(onClick = { dislikeDoggo() }) {
          Icon(Icons.Rounded.Close, "Dislike icon")
          Spacer(modifier = Modifier.width(8.dp))
          Text("Dislike")
        }
        Button(onClick = { likeDoggo() }) {
          Icon(Icons.Rounded.Favorite, "Heart icon")
          Spacer(modifier = Modifier.width(8.dp))
          Text("Like")
        }
      }
    }
  }

  private fun likeDoggo() {
    submitRating(DoggoRating(doggoImageUrl, true))
  }

  private fun dislikeDoggo() {
    submitRating(DoggoRating(doggoImageUrl, false))
  }
}

@Preview
@Composable
private fun RatingStep_Preview() =
  PreviewNavigable {
    RatingStep("https://example.com/doggo", submitRating = { })
  }
