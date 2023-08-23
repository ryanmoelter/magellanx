package com.ryanmoelter.magellanx.doggos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ryanmoelter.magellanx.compose.setContentNavigable
import com.ryanmoelter.magellanx.doggos.home.RootJourney

class MainActivity : ComponentActivity() {

  lateinit var rootJourney: RootJourney

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    rootJourney = injector.rootJourney

    setContentNavigable(rootJourney)
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(
    text = "Hello $name!",
    modifier = modifier
  )
}

