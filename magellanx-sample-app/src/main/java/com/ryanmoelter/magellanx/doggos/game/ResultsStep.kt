package com.ryanmoelter.magellanx.doggos.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ryanmoelter.magellanx.compose.ComposeStep
import com.ryanmoelter.magellanx.doggos.ui.preview.PreviewNavigable

class ResultsStep(
  private val doggoRatings: List<DoggoRating>,
  private val goHome: () -> Unit,
) : ComposeStep() {
  @Composable
  override fun Content() {
    Column {
      Column(
        Modifier
          .verticalScroll(rememberScrollState())
          .weight(1f),
      ) {
        val numCorrect = doggoRatings.count { it.liked }
        val possibleCorrect = doggoRatings.size

        Spacer(modifier = Modifier.height(32.dp))

        Text(
          text = "You got $numCorrect out of $possibleCorrect correct",
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
          style = MaterialTheme.typography.displaySmall,
          textAlign = TextAlign.Center,
        )

        HorizontalDivider(Modifier.padding(vertical = 8.dp))

        doggoRatings.forEach {
          Row(
            modifier =
              Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            Text(if (it.liked) "✅" else "❌")
            AsyncImage(
              model = it.doggoImageUrl,
              contentDescription = "Doggo image",
              modifier = Modifier.height(80.dp),
            )
          }
        }
      }

      HorizontalDivider()
      Row(
        Modifier
          .fillMaxWidth()
          .padding(all = 16.dp),
        horizontalArrangement = Arrangement.End,
      ) {
        Button(onClick = { goHome() }) {
          Text(text = "Take me home")
          Spacer(modifier = Modifier.width(4.dp))
          Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null)
        }
      }
    }
  }
}

@Preview
@Composable
private fun ResultsStep_Preview() =
  PreviewNavigable {
    ResultsStep(
      doggoRatings =
        listOf(
          DoggoRating("https://example.com/doggoImage", true),
          DoggoRating("https://example.com/doggoImage", true),
          DoggoRating("https://example.com/doggoImage", false),
          DoggoRating("https://example.com/doggoImage", true),
          DoggoRating("https://example.com/doggoImage", true),
          DoggoRating("https://example.com/doggoImage", true),
          DoggoRating("https://example.com/doggoImage", true),
          DoggoRating("https://example.com/doggoImage", false),
          DoggoRating("https://example.com/doggoImage", true),
          DoggoRating("https://example.com/doggoImage", true),
        ),
      goHome = { },
    )
  }
