package com.thatwaz.unloadpro.viewmodel



import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.thatwaz.unloadpro.ui.utils.ErrorHandler
import com.thatwaz.unloadpro.ui.utils.FirestoreHelper
import com.thatwaz.unloadpro.ui.utils.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import kotlin.math.ceil


@Serializable
data class BatchDelay(
    val batchNumber: Int,
    val duration: Long,
    val reason: String
)

@HiltViewModel
class UnloadViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    // MutableStateFlow to hold the count of cartons
    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count

    // MutableStateFlow to hold the elapsed time in seconds
    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime: StateFlow<Long> = _elapsedTime

    // MutableStateFlow to hold the elapsed time for the current batch in seconds
    private val _batchElapsedTime = MutableStateFlow(0L)
    val batchElapsedTime: StateFlow<Long> = _batchElapsedTime

    // MutableStateFlow to hold the durations of all completed batches in seconds
    private val _batchTimes = MutableStateFlow<List<Long>>(emptyList())
    val batchTimes : StateFlow<List<Long>> = _batchTimes

    // MutableStateFlow to hold the duration of the last completed batch in seconds
    private val _lastBatchDuration = MutableStateFlow(0L)
    val lastBatchDuration: StateFlow<Long> = _lastBatchDuration

    // MutableStateFlow to hold the calculated average cartons per hour
    private val _averageCartonsPerHour = MutableStateFlow(0)
    val averageCartonsPerHour: StateFlow<Int> = _averageCartonsPerHour

    // MutableStateFlow to hold the timestamp of the last batch addition
    private val _lastBatchTimeStamp = MutableStateFlow("")
    val lastBatchTimeStamp: StateFlow<String> = _lastBatchTimeStamp

    // MutableStateFlow to hold whether the alert dialog should be shown
    private val _showAlertDialog = MutableStateFlow(false)
    val showAlertDialog: StateFlow<Boolean> = _showAlertDialog

    private var lastBatchTime = 0L
    private var initialCartonCount: Int = 0
    private var timerJob: Job? = null
    private var batchTimerJob: Job? = null

    // MutableStateFlow to hold the list of batch delays
//    private val _batchDelays = MutableStateFlow<List<BatchDelay>>(emptyList())
//    val batchDelays: StateFlow<List<BatchDelay>> = _batchDelays


    private var currentBatchNumber = 1


    private val _batchDelays = MutableStateFlow<List<BatchDelay>>(emptyList())
    val batchDelays: StateFlow<List<BatchDelay>> get() = _batchDelays
    private var piecesProcessed = 0
    fun getSerializedBatchTimes(): String {
        return Json.encodeToString(_batchTimes.value)
    }

    private var timersStarted = false
    private var initialCountSet = false

    private var delaySequence = 1  // This will keep track of the sequence of delays

    init {
        listenToCountChanges()
//        addBatchDelay("DOH!")
//        startTimer() // Ensure the timer starts when the ViewModel is initialized
//        startBatchTimer()
    }


    fun getSerializedBatchDelays(): String {
        return Json.encodeToString(_batchDelays.value)
    }


    fun addBatchDelay(reason: String) {
        val duration = _lastBatchDuration.value
        piecesProcessed = initialCartonCount - _count.value
        val batchDelay = BatchDelay(piecesProcessed, duration, reason)
        Log.i("DOH!", "Adding delay: $batchDelay")
        _batchDelays.value = _batchDelays.value + batchDelay
        _showAlertDialog.value = false
        Log.i("DOH!", "Delays after adding: ${_batchDelays.value}")
    }

//    fun addBatchDelay(reason: String) {
//        val duration = _lastBatchDuration.value
//        val batchDelay = BatchDelay(currentBatchNumber, duration, reason)
//        Log.i("DOH!", "Adding delay: $batchDelay")
//        _batchDelays.value = _batchDelays.value + batchDelay
//        currentBatchNumber++
//        _showAlertDialog.value = false
//        Log.i("DOH!", "Delays after adding: ${_batchDelays.value}")
//    }


    private fun listenToCountChanges() {
        FirestoreHelper.getCountRef(firestore).addSnapshotListener { snapshot, e ->
            if (e != null) {
                ErrorHandler.handleError(e)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val newCount = snapshot.getLong("count")?.toInt() ?: 0
                _count.value = newCount
            }
        }
    }

    private fun updateEstimatedCompletionTime() {
        val estimatedCompletionTime = TimeUtils.getEstimatedCompletionTime(_count.value, _batchTimes.value)
        viewModelScope.launch {
            try {
                FirestoreHelper.updateEstimatedCompletionTime(firestore, estimatedCompletionTime)
            } catch (e: Exception) {
                ErrorHandler.handleError(e)
            }
        }
    }


    // Reset the batch number when resetting the count

    // Update resetCount to reset batch number and other relevant fields

    fun resetCount(initialCartonCount: Int) {
        if (!initialCountSet) {
            this.initialCartonCount = initialCartonCount
            initialCountSet = true
            piecesProcessed = 0  // Reset pieces processed
            viewModelScope.launch {
                try {
                    FirestoreHelper.updateCount(firestore, initialCartonCount)
                    _count.value = initialCartonCount
                    lastBatchTime = System.currentTimeMillis()
                    _batchTimes.value = emptyList() // Reset batch times
                    resetBatchTimer()
                } catch (e: Exception) {
                    ErrorHandler.handleError(e)
                }
            }
        }
    }




    // Method to handle batch time addition
    fun addBatchTime(batchTime: Long) {
        _batchTimes.value = _batchTimes.value + batchTime
        Log.i("DOH!","Times of each batch are: ${_batchTimes.value}")
    }

    fun decrementCount() {
        viewModelScope.launch {
            try {
                val currentCount = _count.value
                if (currentCount > 0) {
                    val currentTime = System.currentTimeMillis()
                    val batchDuration = (currentTime - lastBatchTime) / 1000
                    _batchTimes.value = _batchTimes.value + batchDuration
                    _lastBatchDuration.value = batchDuration
                    lastBatchTime = currentTime
                    _lastBatchTimeStamp.value = TimeUtils.getCurrentTimeString(currentTime)
                    FirestoreHelper.updateCount(firestore, currentCount - 100)
                    _count.value = currentCount - 100
                    resetBatchTimer()
                    updateAverageCartonsPerHour()
                    updateEstimatedCompletionTime()
                    checkBatchDuration(batchDuration)
                }
            } catch (e: Exception) {
                ErrorHandler.handleError(e)
            }
        }
    }

//    fun decrementCount() {
//        viewModelScope.launch {
//            try {
//                val currentCount = _count.value
//                if (currentCount > 0) {
//                    val currentTime = System.currentTimeMillis()
//                    val batchDuration = (currentTime - lastBatchTime) / 1000
//                    _batchTimes.value = _batchTimes.value + batchDuration
//                    _lastBatchDuration.value = batchDuration
//                    lastBatchTime = currentTime
//                    _lastBatchTimeStamp.value = TimeUtils.getCurrentTimeString(currentTime)
//                    FirestoreHelper.updateCount(firestore, currentCount - 100)
//                    _count.value = currentCount - 100
//                    resetBatchTimer()
//                    updateAverageCartonsPerHour()
//                    updateEstimatedCompletionTime()
//                    checkBatchDuration(batchDuration)
//                }
//            } catch (e: Exception) {
//                ErrorHandler.handleError(e)
//            }
//        }
//    }

    private fun checkBatchDuration(batchDuration: Long) {
        if (batchDuration > 1 * 2) { // 8 minutes in seconds for production, 2 seconds for testing
            Log.i("DOH!","Batch duration is $batchDuration")
            _showAlertDialog.value = true
        }
    }


    fun startTimer() {
        if (timerJob == null) {
            timerJob = viewModelScope.launch {
                while (true) {
                    delay(1000L)
                    _elapsedTime.value += 1L
                }
            }
        }
    }


    fun startBatchTimer() {
        if (batchTimerJob == null) {
            batchTimerJob = viewModelScope.launch {
                while (true) {
                    delay(1000L)
                    _batchElapsedTime.value += 1L
                }
            }
        }
    }




    fun stopTimer() {
        timerJob?.cancel()
        batchTimerJob?.cancel()
    }


    fun resetBatchTimer() {
        _batchElapsedTime.value = 0L
        startBatchTimer()
    }

    fun dismissAlertDialog() {
        _showAlertDialog.value = false
    }

    private fun updateAverageCartonsPerHour() {
        val totalSeconds = _elapsedTime.value
        val processedCartons = initialCartonCount - _count.value
        if (totalSeconds > 0) {
            val cartonsPerHour = ceil((processedCartons.toDouble() / totalSeconds) * 3600).toInt()
            _averageCartonsPerHour.value = cartonsPerHour
        } else {
            _averageCartonsPerHour.value = 0
        }
    }
}








//@HiltViewModel
//class UnloadViewModel @Inject constructor(
//    private val firestore: FirebaseFirestore
//) : ViewModel() {
//    // Firestore references
//    private val countRef = firestore.collection("counts").document("clickCount")
//    private val estCompletionTimeRef = firestore.collection("completionTimes").document("estimatedCompletionTime")
//
//    // MutableStateFlow to hold the count of cartons
//    private val _count = MutableStateFlow(0)
//    val count: StateFlow<Int> = _count
//
//    // MutableStateFlow to hold the elapsed time in seconds
//    private val _elapsedTime = MutableStateFlow(0L)
//    val elapsedTime: StateFlow<Long> = _elapsedTime
//
//    // MutableStateFlow to hold the elapsed time for the current batch in seconds
//    private val _batchElapsedTime = MutableStateFlow(0L)
//    val batchElapsedTime: StateFlow<Long> = _batchElapsedTime
//
//    // MutableStateFlow to hold the durations of all completed batches in seconds
//    private val _batchTimes = MutableStateFlow<List<Long>>(emptyList())
////    val batchTimes: StateFlow<List<Long>> = _batchTimes
//
//    // MutableStateFlow to hold the duration of the last completed batch in seconds
//    private val _lastBatchDuration = MutableStateFlow(0L)
//    val lastBatchDuration: StateFlow<Long> = _lastBatchDuration
//
//    // MutableStateFlow to hold the calculated average cartons per hour
//    private val _averageCartonsPerHour = MutableStateFlow(0)
//    val averageCartonsPerHour: StateFlow<Int> = _averageCartonsPerHour
//
//    // MutableStateFlow to hold the timestamp of the last batch addition
//    private val _lastBatchTimeStamp = MutableStateFlow("")
//    val lastBatchTimeStamp: StateFlow<String> = _lastBatchTimeStamp
//
//    private var lastBatchTime = 0L
//    private var initialCartonCount: Int = 0
//    private var timerJob: Job? = null
//    private var batchTimerJob: Job? = null
//
//    init {
//        listenToCountChanges()
//    }
//
//    private fun listenToCountChanges() {
//        countRef.addSnapshotListener { snapshot, e ->
//            if (e != null) {
//                ErrorHandler.handleError(e)
//                return@addSnapshotListener
//            }
//            if (snapshot != null && snapshot.exists()) {
//                val newCount = snapshot.getLong("count")?.toInt() ?: 0
//                _count.value = newCount
//            }
//        }
//    }
//
//    //Firebase
//    private fun updateEstimatedCompletionTime() {
//        val estimatedCompletionTime = getEstimatedCompletionTime()
//        estCompletionTimeRef.set(mapOf("time" to estimatedCompletionTime))
//    }
//
//    fun resetCount(initialCartonCount: Int) {
//        this.initialCartonCount = initialCartonCount
//        viewModelScope.launch {
//            try {
//                countRef.set(mapOf("count" to initialCartonCount))
//                _count.value = initialCartonCount
//                lastBatchTime = System.currentTimeMillis()
//                _batchTimes.value = emptyList() // Reset batch times
//                resetBatchTimer()
//            } catch (e: Exception) {
//                ErrorHandler.handleError(e)
//            }
//        }
//    }
//
//    fun decrementCount() {
//        viewModelScope.launch {
//            try {
//                val currentCount = _count.value
//                if (currentCount > 0) {
//                    val currentTime = System.currentTimeMillis()
//                    val batchDuration = (currentTime - lastBatchTime) / 1000
//                    _batchTimes.value = _batchTimes.value + batchDuration
//                    _lastBatchDuration.value = batchDuration
//                    lastBatchTime = currentTime
//                    _lastBatchTimeStamp.value = getCurrentTimeString(currentTime)
//                    countRef.set(mapOf("count" to currentCount - 100))
//                    _count.value = currentCount - 100
//                    resetBatchTimer()
//                    updateAverageCartonsPerHour()
//                    updateEstimatedCompletionTime()
//                }
//            } catch (e: Exception) {
//                ErrorHandler.handleError(e)
//            }
//        }
//    }
//
//    private fun getCurrentTimeString(currentTime: Long): String {
//        val formatter = SimpleDateFormat("h:mm a", Locale.getDefault())
//        return formatter.format(Date(currentTime))
//    }
//
//    fun startTimer() {
//        timerJob?.cancel()
//        timerJob = viewModelScope.launch {
//            _elapsedTime.value = 0L
//            while (true) {
//                delay(1000L)
//                _elapsedTime.value += 1L
//            }
//        }
//        startBatchTimer()
//    }
//
//    fun stopTimer() {
//        timerJob?.cancel()
//        batchTimerJob?.cancel()
//    }
//
//    fun startBatchTimer() {
//        batchTimerJob?.cancel()
//        batchTimerJob = viewModelScope.launch {
//            _batchElapsedTime.value = 0L
//            while (true) {
//                delay(1000L)
//                _batchElapsedTime.value += 1L
//            }
//        }
//    }
//
//    fun resetBatchTimer() {
//        _batchElapsedTime.value = 0L
//        startBatchTimer()
//    }
//
//    fun getEstimatedCompletionTime(): String {
//        return try {
//            val averageSeconds = if (_batchTimes.value.isNotEmpty()) _batchTimes.value.average() else 0.0
//            val numberOfBatches = (_count.value + 99) / 100
//            val estimatedNumberOfSecondsToCompletion = numberOfBatches * averageSeconds
//            val estimatedNumberOfMinutesToCompletion = estimatedNumberOfSecondsToCompletion / 60
//            val calendar = Calendar.getInstance()
//            calendar.add(Calendar.MINUTE, estimatedNumberOfMinutesToCompletion.toInt())
//            val estimatedCompletionTime = calendar.time
//            val formatter = SimpleDateFormat("h:mm a", Locale.getDefault())
//            formatter.format(estimatedCompletionTime)
//        } catch (e: Exception) {
//            ErrorHandler.handleError(e)
//            "Error"
//        }
//    }
//
//    fun getAverageBatchTime(): String {
//        return try {
//            val averageSeconds = if (_batchTimes.value.isNotEmpty()) _batchTimes.value.average() else 0.0
//            val minutes = averageSeconds.toInt() / 60
//            val seconds = averageSeconds.toInt() % 60
//            if (minutes > 0) {
//                String.format("%dm %ds", minutes, seconds)
//            } else {
//                String.format("%ds", seconds)
//            }
//        } catch (e: Exception) {
//            ErrorHandler.handleError(e)
//            "Error"
//        }
//    }
//    fun formatBatchDuration(duration: Long): String {
//        val minutes = duration / 60
//        val seconds = duration % 60
//        return if (minutes > 0) {
//            String.format("%dm %ds", minutes, seconds)
//        } else {
//            String.format("%ds", seconds)
//        }
//    }
//
//    private fun updateAverageCartonsPerHour() {
//        val totalSeconds = _elapsedTime.value
//        val processedCartons = initialCartonCount - _count.value
//        if (totalSeconds > 0) {
//            val cartonsPerHour = ceil((processedCartons.toDouble() / totalSeconds) * 3600).toInt()
//            _averageCartonsPerHour.value = cartonsPerHour
//        } else {
//            _averageCartonsPerHour.value = 0
//        }
//    }

//    fun getAverageCartonsPerHour(): String {
//        return try {
//            val totalSeconds = _elapsedTime.value
//            val processedCartons = initialCartonCount - _count.value
//            if (totalSeconds > 0) {
//                val cartonsPerHour = ceil((processedCartons.toDouble() / totalSeconds) * 3600).toInt()
//                String.format("%d CPH", cartonsPerHour)
//            } else {
//                "0 CPH"
//            }
//        } catch (e: Exception) {
//            ErrorHandler.handleError(e)
//            "Error"
//        }
//    }
//}




//@HiltViewModel
//class UnloadViewModel @Inject constructor(
//    private val firestore: FirebaseFirestore
//) : ViewModel() {
//    private val countRef = firestore.collection("counts").document("clickCount")
//
//    private val _count = MutableStateFlow(0)
//    val count: StateFlow<Int> = _count
//
//    private val _elapsedTime = MutableStateFlow(0L)
//    val elapsedTime: StateFlow<Long> = _elapsedTime
//
//    private val _batchTimes = MutableStateFlow<List<Long>>(emptyList())
//    val batchTimes: StateFlow<List<Long>> = _batchTimes
//
//    private var lastBatchTime = 0L
//    private var initialCartonCount: Int = 0
//    private var timerJob: Job? = null
//
//    init {
//        listenToCountChanges()
//    }
//
//    private fun listenToCountChanges() {
//        countRef.addSnapshotListener { snapshot, e ->
//            if (e != null) {
//                return@addSnapshotListener
//            }
//            if (snapshot != null && snapshot.exists()) {
//                val newCount = snapshot.getLong("count")?.toInt() ?: 0
//                _count.value = newCount
//            }
//        }
//    }
//
//    fun resetCount(initialCartonCount: Int) {
//        this.initialCartonCount = initialCartonCount
//        viewModelScope.launch {
//            countRef.set(mapOf("count" to initialCartonCount))
//            _count.value = initialCartonCount
//            lastBatchTime = System.currentTimeMillis()
//            _batchTimes.value = emptyList() // Reset batch times
//        }
//    }
//
//    fun decrementCount() {
//        viewModelScope.launch {
//            val currentCount = _count.value
//            if (currentCount > 0) {
//                val currentTime = System.currentTimeMillis()
//                val batchDuration = (currentTime - lastBatchTime) / 1000
//                _batchTimes.value = _batchTimes.value + batchDuration
//                lastBatchTime = currentTime
//                countRef.set(mapOf("count" to currentCount - 100))
//                _count.value = currentCount - 100
//            }
//        }
//    }
//
//    fun startTimer() {
//        timerJob?.cancel()
//        timerJob = viewModelScope.launch {
//            _elapsedTime.value = 0L
//            while (true) {
//                delay(1000L)
//                _elapsedTime.value += 1L
//            }
//        }
//    }
//
//    fun stopTimer() {
//        timerJob?.cancel()
//    }
//
//    fun getEstimatedCompletionTime(): String {
//        return formatEstimatedCompletionTime(_count.value, _batchTimes.value)
//    }
//
//    fun getAverageBatchTime(): String {
//        return formatAverageBatchTime(_batchTimes.value)
//    }
//}







//class UnloadViewModel : ViewModel() {
//    private val firestore = FirebaseFirestore.getInstance()
//    private val countRef = firestore.collection("counts").document("clickCount")
//    private val _count = MutableStateFlow(0)
//    val count: StateFlow<Int> = _count
//
//    init {
//        listenToCountChanges()
//    }
//
//    private fun listenToCountChanges() {
//        countRef.addSnapshotListener { snapshot, e ->
//            if (e != null) {
//                return@addSnapshotListener
//            }
//            if (snapshot != null && snapshot.exists()) {
//                val newCount = snapshot.getLong("count")?.toInt() ?: 0
//                _count.value = newCount
//            }
//        }
//    }
//
//    fun resetCount(initialCartonCount: Int) {
//        viewModelScope.launch {
//            countRef.set(mapOf("count" to initialCartonCount))
//            _count.value = initialCartonCount
//        }
//    }
//
//    fun decrementCount() {
//        viewModelScope.launch {
//            val currentCount = _count.value
//            countRef.set(mapOf("count" to currentCount - 100))
//        }
//    }
//}




//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.google.firebase.firestore.FirebaseFirestore
//import dagger.hilt.android.lifecycle.HiltViewModel
//import javax.inject.Inject
//
//
//@HiltViewModel
//class UnloadViewModel @Inject constructor(
//    private val firestore: FirebaseFirestore // Injected instance of FirebaseFirestore
//) : ViewModel() {
//    private val countRef = firestore.collection("counts").document("clickCount") // Use the injected instance
//    private val _count = MutableLiveData<Int>()
//    val count: LiveData<Int> = _count
//
//    init {
//        resetCount(150)  // Call to reset the count when ViewModel is initialized
//
//        val listenerRegistration = countRef.addSnapshotListener { snapshot, e ->
//            if (e != null) {
//                Log.w("Firestore", "Listen failed.", e)
//                return@addSnapshotListener
//            }
//            if (snapshot != null && snapshot.exists()) {
//                val newCount = snapshot.getLong("count")?.toInt() ?: 0
//                _count.value = newCount
//                Log.d("Firestore", "Count updated to: $newCount")
//            } else {
//                Log.d("Firestore", "Current data: null")
//            }
//        }
//    }
//
//    fun resetCount(initialCartonCount: Int) {
//        countRef.set(mapOf("count" to initialCartonCount))  // Set the count to initialCartonCount
//        _count.value = initialCartonCount
//    }
//
//    fun decrementCount() {
//        val currentCount = _count.value ?: 0
//        countRef.set(mapOf("count" to currentCount - 100))
//    }
//}




