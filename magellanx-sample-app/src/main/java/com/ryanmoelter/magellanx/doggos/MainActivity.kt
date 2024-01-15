package com.ryanmoelter.magellanx.doggos

import android.os.Bundle
import androidx.activity.ComponentActivity
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
