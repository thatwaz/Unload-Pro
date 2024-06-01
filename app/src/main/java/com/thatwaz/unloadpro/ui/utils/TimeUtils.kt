package com.thatwaz.unloadpro.ui.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun formatElapsedTime(elapsedTime: Long): String {
    val minutes = elapsedTime / 60
    val seconds = elapsedTime % 60
    return String.format("%02d:%02d", minutes, seconds)
}

fun formatAverageBatchTime(batchTimes: List<Long>): String {
    val averageSeconds = if (batchTimes.isNotEmpty()) batchTimes.average() else 0.0
    val minutes = averageSeconds.toInt() / 60
    val seconds = averageSeconds.toInt() % 60
    return if (minutes > 0) {
        String.format("%dm %ds", minutes, seconds)
    } else {
        String.format("%ds", seconds)
    }
}

fun formatEstimatedCompletionTime(currentCount: Int, batchTimes: List<Long>): String {
    val averageSeconds = if (batchTimes.isNotEmpty()) batchTimes.average() else 0.0
    val numberOfBatches = (currentCount + 99) / 100
    val estimatedNumberOfSecondsToCompletion = numberOfBatches * averageSeconds
    val estimatedNumberOfMinutesToCompletion = estimatedNumberOfSecondsToCompletion / 60
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.MINUTE, estimatedNumberOfMinutesToCompletion.toInt())
    val estimatedCompletionTime = calendar.time
    val formatter = SimpleDateFormat("h:mm a", Locale.getDefault())
    return formatter.format(estimatedCompletionTime)
}

