package com.thatwaz.unloadpro.ui.presentation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thatwaz.unloadpro.ui.common.CustomButton
import com.thatwaz.unloadpro.viewmodel.ClickCounterViewModel


@Composable
fun DisplayCounterScreen(
    clickCounterViewModel: ClickCounterViewModel,
    initialCartonCount: Int
) {
    // Initialize the count with the initialCartonCount
    LaunchedEffect(Unit) {
        clickCounterViewModel.resetCount(initialCartonCount)
    }

    val count by clickCounterViewModel.count.observeAsState(initialCartonCount)

    Log.d("DisplayCounterScreen", "Current Carton Count: $count")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 180.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "00:00",
                style = MaterialTheme.typography.displayMedium
            )

            Text(
                text = "00:00",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontSize = MaterialTheme.typography.displaySmall.fontSize * 0.7
                )
            )

            Text(
                text = "${count ?: initialCartonCount}",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(top = 32.dp)
            )
            Text(
                text = "Cartons Remaining out of $initialCartonCount",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "Last batch added at 4:55 am",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                DisplayMetric("6m 32s", "Last Batch Speed")
                DisplayMetric("897", "Avg CPH")
                DisplayMetric("659", "Avg Batch Time")
            }

            CustomButton(
                onClick = { clickCounterViewModel.decrementCount() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { /* TODO: Implement action */ }
                ) {
                    Text("Count")
                }
                Button(
                    onClick = { /* TODO: Implement action */ }
                ) {
                    Text("Finalize")
                }
            }

            Text(
                text = "Est. Completion Time is 7:04 AM (27 mins)",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 50.dp)
            )
        }
    }
}

@Composable
fun DisplayMetric(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
    }
}


//@Composable
//fun DisplayCounterScreen(
//    clickCounterViewModel: ClickCounterViewModel,
//    initialCartonCount: Int
//) {
//    val count by clickCounterViewModel.count.observeAsState()
//    var currentCount = initialCartonCount
//
//    Log.d("DisplayCounterScreen", "Initial Carton Count: $initialCartonCount")
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .align(Alignment.TopCenter)
//                .padding(top = 180.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = "00:00",
//                style = MaterialTheme.typography.displayMedium
//            )
//
//            Text(
//                text = "00:00",
//                style = MaterialTheme.typography.displaySmall.copy(
//                    fontSize = MaterialTheme.typography.displaySmall.fontSize * 0.7
//                )
//            )
//
//            Text(
//                text = "$currentCount",
//                style = MaterialTheme.typography.headlineLarge,
//                modifier = Modifier.padding(top = 32.dp)
//            )
//            Text(
//                text = "Cartons Remaining out of $initialCartonCount",
//                style = MaterialTheme.typography.bodyLarge,
//                modifier = Modifier.padding(top = 8.dp)
//            )
//            Text(
//                text = "Last batch added at 4:55 am",
//                style = MaterialTheme.typography.bodySmall,
//                modifier = Modifier.padding(top = 8.dp)
//            )
//
//            Row(
//                horizontalArrangement = Arrangement.SpaceEvenly,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 16.dp)
//            ) {
//                DisplayMetric("6m 32s", "Last Batch Speed")
//                DisplayMetric("897", "Avg CPH")
//                DisplayMetric("659", "Avg Batch Time")
//            }
//
//            CustomButton(
//                onClick = { clickCounterViewModel.decrementCount() },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 50.dp)
//            )
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 25.dp),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Button(
//                    onClick = { /* TODO: Implement action */ }
//                ) {
//                    Text("Count")
//                }
//                Button(
//                    onClick = { /* TODO: Implement action */ }
//                ) {
//                    Text("Finalize")
//                }
//            }
//
//            Text(
//                text = "Est. Completion Time is 7:04 AM (27 mins)",
//                style = MaterialTheme.typography.bodyLarge,
//                modifier = Modifier.padding(top = 50.dp)
//            )
//        }
//    }
//}
//
//@Composable
//fun DisplayMetric(value: String, label: String) {
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Text(
//            text = value,
//            style = MaterialTheme.typography.bodyMedium,
//            modifier = Modifier.padding(bottom = 4.dp)
//        )
//        Text(
//            text = label,
//            style = MaterialTheme.typography.bodySmall
//        )
//    }
//}




//@Composable
//fun DisplayCounterScreen(
//    clickCounterViewModel: ClickCounterViewModel,
//    mainViewModel: MainViewModel
//) {
//    val count by clickCounterViewModel.count.observeAsState()
//    val initialCartonCount by mainViewModel.initialCartonCount.observeAsState(500)
//    val thisCount = mainViewModel.initialCartonCount.value
//
//    Log.d("DisplayCounterScreen", "Initial Carton Count: $initialCartonCount")
//    Log.d("DisplayCounterScreen", "Initial Carton Count: $thisCount")
//
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .align(Alignment.TopCenter)
//                .padding(top = 180.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = "00:00",
//                style = MaterialTheme.typography.displayMedium
//            )
//
//            Text(
//                text = "00:00",
//                style = MaterialTheme.typography.displaySmall.copy(
//                    fontSize = MaterialTheme.typography.displaySmall.fontSize * 0.7
//                )
//            )
//
//            Text(
//                text = "${count ?: 0}",
//                style = MaterialTheme.typography.headlineLarge,
//                modifier = Modifier.padding(top = 32.dp)
//            )
//            Text(
//                text = "Cartons Remaining out of $initialCartonCount",
//                style = MaterialTheme.typography.bodyLarge,
//                modifier = Modifier.padding(top = 8.dp)
//            )
//            Text(
//                text = "Last batch added at 4:55 am",
//                style = MaterialTheme.typography.bodySmall,
//                modifier = Modifier.padding(top = 8.dp)
//            )
//
//            Row(
//                horizontalArrangement = Arrangement.SpaceEvenly,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 16.dp)
//            ) {
//                DisplayMetric("6m 32s", "Last Batch Speed")
//                DisplayMetric("897", "Avg CPH")
//                DisplayMetric("659", "Avg Batch Time")
//            }
//
//            CustomButton(
//                onClick = { clickCounterViewModel.decrementCount() },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 50.dp)
//            )
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 25.dp),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Button(
//                    onClick = { /* TODO: Implement action */ }
//                ) {
//                    Text("Count")
//                }
//                Button(
//                    onClick = { /* TODO: Implement action */ }
//                ) {
//                    Text("Finalize")
//                }
//            }
//
//            Text(
//                text = "Est. Completion Time is 7:04 AM (27 mins)",
//                style = MaterialTheme.typography.bodyLarge,
//                modifier = Modifier.padding(top = 50.dp)
//            )
//        }
//    }
//}
//
//@Composable
//fun DisplayMetric(value: String, label: String) {
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Text(
//            text = value,
//            style = MaterialTheme.typography.bodyMedium,
//            modifier = Modifier.padding(bottom = 4.dp)
//        )
//        Text(
//            text = label,
//            style = MaterialTheme.typography.bodySmall
//        )
//    }
//}






















