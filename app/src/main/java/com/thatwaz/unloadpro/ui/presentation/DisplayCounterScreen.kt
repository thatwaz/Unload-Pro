package com.thatwaz.unloadpro.ui.presentation



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thatwaz.unloadpro.viewmodel.ClickCounterViewModel


@Composable
fun DisplayCounterScreen(viewModel: ClickCounterViewModel = viewModel()) {
    val count = viewModel.count.observeAsState(0) // Observe count from ViewModel

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Clicks: ${count.value}",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Button(
            onClick = { viewModel.incrementCount() }
        ) {
            Text("Increment")
        }
    }
}

