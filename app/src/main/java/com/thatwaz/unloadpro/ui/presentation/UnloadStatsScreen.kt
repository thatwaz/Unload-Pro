package com.thatwaz.unloadpro.ui.presentation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thatwaz.unloadpro.ui.utils.TimeUtils
import com.thatwaz.unloadpro.viewmodel.BatchDelay
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json



@Composable
fun UnloadStatsScreen(
    batchDelaysJson: String
) {
//    val batchTimes by unloadViewModel.batchTimes.collectAsStateWithLifecycle()
    val batchDelays: List<BatchDelay> = Json.decodeFromString(batchDelaysJson)


    LaunchedEffect(batchDelays) {
        Log.i("UnloadStatsScreen", "Batch Times are $batchDelays")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Unload Stats",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Headers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Batch", style = MaterialTheme.typography.bodySmall)
            Text(text = "Time", style = MaterialTheme.typography.bodySmall)
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

//         Display each batch time
        // Display each batch delay
        batchDelays.forEach { delay ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = delay.batchNumber.toString(), style = MaterialTheme.typography.bodySmall)
                Text(text = TimeUtils.formatBatchDuration(delay.duration), style = MaterialTheme.typography.bodySmall)
                Text(text = delay.reason, style = MaterialTheme.typography.bodySmall)
            }
            Divider(modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}


//@Composable
//fun UnloadStatsScreen(
//    unloadViewModel: UnloadViewModel
//) {
//
//    LaunchedEffect(Unit) {
//        var batchDelays = unloadViewModel.batchDelays.value
//        Log.i("UnloadStatsScreen", "me batchDelays are: $batchDelays")
//    }
//
//    Log.i("UnloadStatsScreen", "Composable recomposed")
//    val batchDelays by unloadViewModel.batchDelays.collectAsState()
//
//
//    Log.i("UnloadStatsScreen", "Observed batchDelays: $batchDelays")
//
//    LaunchedEffect(batchDelays) {
//        Log.i("UnloadStatsScreen", "Unload Delays are $batchDelays")
////        unloadViewModel.batchDelays
//    }
//
//    // Add a simple UI to see if recomposition happens
//    Text(text = "Current Batch Delays: $batchDelays")
////    val batchDelays by unloadViewModel.batchDelays.collectAsState()
////
////    Log.i("UnloadStatsScreen", "Observed batchDelays: $batchDelays")
////
////    LaunchedEffect(batchDelays) {
////        Log.i("UnloadStatsScreen", "Unload Delays are $batchDelays")
////    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Text(
//            text = "Unload Stats",
//            style = MaterialTheme.typography.bodyMedium,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        // Headers
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(text = "Batch", style = MaterialTheme.typography.bodySmall)
//            Text(text = "Time", style = MaterialTheme.typography.bodySmall, color = Color.Black)
//            Text(text = "Reason", style = MaterialTheme.typography.bodySmall)
//        }
//
//        Divider(modifier = Modifier.padding(vertical = 8.dp))
//
//        // Display each batch delay
//        batchDelays.forEach { delay ->
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(text = delay.batchNumber.toString(), style = MaterialTheme.typography.bodySmall)
//                Text(text = TimeUtils.formatBatchDuration(delay.duration), style = MaterialTheme.typography.bodySmall)
//                Text(text = delay.reason, style = MaterialTheme.typography.bodySmall)
//            }
//            Divider(modifier = Modifier.padding(vertical = 4.dp))
//        }
//    }
//}


//@Composable
//fun UnloadStatsScreen(
//    unloadViewModel: UnloadViewModel
//) {
//    val batchDelays by unloadViewModel.batchDelays.collectAsState()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Text(
//            text = "Unload Stats",
//            style = MaterialTheme.typography.bodyMedium,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        // Headers
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(text = "Batch", style = MaterialTheme.typography.bodySmall)
//            Text(text = "Time", style = MaterialTheme.typography.bodySmall)
//            Text(text = "Reason", style = MaterialTheme.typography.bodySmall)
//        }
//
//        Divider(modifier = Modifier.padding(vertical = 8.dp))
//
//        // Display each batch delay
//        batchDelays.forEach { delay ->
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(text = delay.batchNumber.toString(), style = MaterialTheme.typography.bodyMedium)
//                Text(text = TimeUtils.formatBatchDuration(delay.duration), style = MaterialTheme.typography.bodyMedium)
//                Text(text = delay.reason, style = MaterialTheme.typography.bodyMedium)
//            }
//            Divider(modifier = Modifier.padding(vertical = 4.dp))
//        }
//    }
//}


//@Composable
//fun UnloadStatsScreen(
//    unloadViewModel: UnloadViewModel,
//) {
//    val batchDelays by unloadViewModel.batchDelays.collectAsState()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Text(
//            text = "Unload Stats",
//            style = MaterialTheme.typography.bodySmall,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        // Labels
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(text = "Batch", style = MaterialTheme.typography.bodyMedium)
//            Text(text = "Time", style = MaterialTheme.typography.bodyMedium)
//            Text(text = "Reason", style = MaterialTheme.typography.bodyMedium)
//        }
//
//        // Display batch delays
//        batchDelays.filter { it.duration > 1 * 60 }.forEach { delay -> // 8 minutes for production, 1 minute for testing
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 8.dp),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(text = "Batch ${delay.batchNumber}")
//                Text(text = TimeUtils.formatBatchDuration(delay.duration))
//                Text(text = delay.reason)
//            }
//        }
//    }
//}
