package com.thatwaz.unloadpro.ui.presentation


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thatwaz.unloadpro.ui.common.CustomButton
import com.thatwaz.unloadpro.ui.theme.LightSilver
import com.thatwaz.unloadpro.ui.utils.formatElapsedTime
import com.thatwaz.unloadpro.viewmodel.UnloadViewModel


@Composable
fun UnloadScreen(
    unloadViewModel: UnloadViewModel,
    initialCartonCount: Int
) {


    var isStarted by remember { mutableStateOf(false) }
    val count by unloadViewModel.count.collectAsState()
    val elapsedTime by unloadViewModel.elapsedTime.collectAsState()


    LaunchedEffect(isStarted) {
        if (isStarted) {
            unloadViewModel.resetCount(initialCartonCount)
            unloadViewModel.startTimer()
        } else {
            unloadViewModel.stopTimer()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Full screen content
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 180.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = formatElapsedTime(elapsedTime),
                style = MaterialTheme.typography.displayMedium
            )

            Text(
                text = "00:00",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontSize = MaterialTheme.typography.displaySmall.fontSize * 0.7
                )
            )

            Text(
                text = "$count",
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
                DisplayMetric(unloadViewModel.getAverageBatchTime(), "Avg Batch Time")
            }

            CustomButton(
                onClick = { unloadViewModel.decrementCount() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, start = 25.dp, end = 25.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { /* TODO: Implement action */ },
                    modifier = Modifier.padding(start = 25.dp)
                ) {
                    Text("Count")
                }
                Button(
                    onClick = { /* TODO: Implement action */ },
                    modifier = Modifier.padding(end = 25.dp)
                ) {
                    Text("Finalize")
                }
            }

            Text(
                text = "Est. Completion Time is ${unloadViewModel.getEstimatedCompletionTime()}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 50.dp)
            )
        }

        // Overlay to mute the screen before starting unload
        if (!isStarted) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightSilver.copy(alpha = 0.8f))
                    .clickable(enabled = false, onClick = {}),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = { isStarted = true }) {
                    Text("Start Unload")
                }
            }
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



//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.livedata.observeAsState
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import com.thatwaz.unloadpro.ui.common.CustomButton
//import com.thatwaz.unloadpro.ui.theme.LightSilver
//import com.thatwaz.unloadpro.viewmodel.UnloadViewModel
//
//@Composable
//fun UnloadScreen(
//    unloadViewModel: UnloadViewModel,
//    initialCartonCount: Int
//) {
//    var isStarted by remember { mutableStateOf(false) }
//    val count by unloadViewModel.count.observeAsState(initialCartonCount)
//
//    LaunchedEffect(isStarted) {
//        if (isStarted) {
//            unloadViewModel.resetCount(initialCartonCount)
//        }
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//                .background(if (isStarted) Color.White else LightSilver.copy(alpha = 0.9f)),
//        contentAlignment = Alignment.Center
//    ) {
//        if (!isStarted) {
//            Button(onClick = { isStarted = true }) {
//                Text("Start Unload")
//            }
//        } else {
//            Column(
//                modifier = Modifier
//                    .align(Alignment.TopCenter)
//                    .padding(top = 180.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    text = "00:00",
//                    style = MaterialTheme.typography.displayMedium
//                )
//
//                Text(
//                    text = "00:00",
//                    style = MaterialTheme.typography.displaySmall.copy(
//                        fontSize = MaterialTheme.typography.displaySmall.fontSize * 0.7
//                    )
//                )
//
//                Text(
//                    text = "$count",
//                    style = MaterialTheme.typography.headlineLarge,
//                    modifier = Modifier.padding(top = 32.dp)
//                )
//                Text(
//                    text = "Cartons Remaining out of $initialCartonCount",
//                    style = MaterialTheme.typography.bodyLarge,
//                    modifier = Modifier.padding(top = 8.dp)
//                )
//                Text(
//                    text = "Last batch added at 4:55 am",
//                    style = MaterialTheme.typography.bodySmall,
//                    modifier = Modifier.padding(top = 8.dp)
//                )
//
//                Row(
//                    horizontalArrangement = Arrangement.SpaceEvenly,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 16.dp)
//                ) {
//                    DisplayMetric("6m 32s", "Last Batch Speed")
//                    DisplayMetric("897", "Avg CPH")
//                    DisplayMetric("659", "Avg Batch Time")
//                }
//
//                CustomButton(
//                    onClick = { unloadViewModel.decrementCount() },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 50.dp,start = 25.dp, end = 25.dp)
//                )
//
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 25.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Button(
//                        onClick = { /* TODO: Implement action */ },
//                        modifier = Modifier
//                            .padding(start = 25.dp)
//                    ) {
//                        Text("Count")
//                    }
//                    Button(
//                        onClick = { /* TODO: Implement action */ },
//                        modifier = Modifier.padding(end = 25.dp)
//                    ) {
//                        Text("Finalize")
//                    }
//                }
//
//                Text(
//                    text = "Est. Completion Time is 7:04 AM (27 mins)",
//                    style = MaterialTheme.typography.bodyLarge,
//                    modifier = Modifier.padding(top = 50.dp)
//                )
//            }
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
//fun CustomButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
//    Button(
//        onClick = onClick,
//        modifier = modifier
//    ) {
//        Text("Decrement")
//    }
//}


//@Composable
//fun UnloadScreen(
//    unloadViewModel: UnloadViewModel,
//    initialCartonCount: Int
//) {
//    // Initialize the count with the initialCartonCount
//    LaunchedEffect(Unit) {
//        unloadViewModel.resetCount(initialCartonCount)
//    }
//
//    val count by unloadViewModel.count.observeAsState(initialCartonCount)
//
//    Log.d("DisplayCounterScreen", "Current Carton Count: $count")
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
//                text = "${count ?: initialCartonCount}",
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
//                onClick = { unloadViewModel.decrementCount() },
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
























