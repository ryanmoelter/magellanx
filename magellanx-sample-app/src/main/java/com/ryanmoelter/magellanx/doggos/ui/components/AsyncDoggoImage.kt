package com.ryanmoelter.magellanx.doggos.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

@Composable
fun AsyncDoggoImage(
  imageUrl: String,
  modifier: Modifier = Modifier,
) {
  SubcomposeAsyncImage(
    model =
      ImageRequest.Builder(LocalContext.current)
        .data(imageUrl)
        .crossfade(true)
        .build(),
    contentDescription = "A good doggo",
    modifier = modifier,
    loading = {
      CircularProgressIndicator(modifier = Modifier.wrapContentSize())
    },
    error = {
      Row(
        modifier = Modifier.wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Icon(
          imageVector = Icons.Filled.Close,
          contentDescription = "Image failed to load",
          modifier = Modifier.size(32.dp),
        )
        Text(text = "Image failed to load")
      }
    },
  )
}
