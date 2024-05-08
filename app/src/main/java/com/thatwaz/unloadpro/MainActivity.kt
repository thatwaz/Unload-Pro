package com.thatwaz.unloadpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thatwaz.unloadpro.ui.presentation.DisplayCounterScreen
import com.thatwaz.unloadpro.viewmodel.ClickCounterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppContent()
        }
    }
}

@Composable
fun AppContent() {
    // Create an instance of the ViewModel
    val viewModel: ClickCounterViewModel = viewModel()
    DisplayCounterScreen(viewModel = viewModel)
}