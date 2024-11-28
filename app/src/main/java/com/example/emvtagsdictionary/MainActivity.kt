package com.example.emvtagsdictionary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.emvtagsdictionary.emvTagModule.view.EMVTagListScreen
import com.example.emvtagsdictionary.emvTagModule.viewModel.EMVTagViewModel
import com.example.emvtagsdictionary.splashScreen.view.SplashScreen
import com.example.emvtagsdictionary.ui.theme.EMVTagsDictionaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EMVTagsDictionaryTheme {
                val viewModel: EMVTagViewModel = viewModel()
                MainScreen(viewModel)
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: EMVTagViewModel) {
    var showSplashScreen by remember { mutableStateOf(true) }

    if (showSplashScreen) {
        SplashScreen(onTimeout = {
            showSplashScreen = false
        })
    } else {
        EMVTagListScreen(viewModel, LocalContext.current)
    }
}
