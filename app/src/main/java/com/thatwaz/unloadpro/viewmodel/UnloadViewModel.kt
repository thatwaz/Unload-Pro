package com.thatwaz.unloadpro.viewmodel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.thatwaz.unloadpro.ui.utils.formatAverageBatchTime
import com.thatwaz.unloadpro.ui.utils.formatEstimatedCompletionTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnloadViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {
    private val countRef = firestore.collection("counts").document("clickCount")

    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count

    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime: StateFlow<Long> = _elapsedTime

    private val _batchTimes = MutableStateFlow<List<Long>>(emptyList())
    val batchTimes: StateFlow<List<Long>> = _batchTimes

    private var lastBatchTime = 0L
    private var initialCartonCount: Int = 0
    private var timerJob: Job? = null

    init {
        listenToCountChanges()
    }

    private fun listenToCountChanges() {
        countRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val newCount = snapshot.getLong("count")?.toInt() ?: 0
                _count.value = newCount
            }
        }
    }

    fun resetCount(initialCartonCount: Int) {
        this.initialCartonCount = initialCartonCount
        viewModelScope.launch {
            countRef.set(mapOf("count" to initialCartonCount))
            _count.value = initialCartonCount
            lastBatchTime = System.currentTimeMillis()
            _batchTimes.value = emptyList() // Reset batch times
        }
    }

    fun decrementCount() {
        viewModelScope.launch {
            val currentCount = _count.value
            if (currentCount > 0) {
                val currentTime = System.currentTimeMillis()
                val batchDuration = (currentTime - lastBatchTime) / 1000
                _batchTimes.value = _batchTimes.value + batchDuration
                lastBatchTime = currentTime
                countRef.set(mapOf("count" to currentCount - 100))
                _count.value = currentCount - 100
            }
        }
    }

    fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            _elapsedTime.value = 0L
            while (true) {
                delay(1000L)
                _elapsedTime.value += 1L
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
    }

    fun getEstimatedCompletionTime(): String {
        return formatEstimatedCompletionTime(_count.value, _batchTimes.value)
    }

    fun getAverageBatchTime(): String {
        return formatAverageBatchTime(_batchTimes.value)
    }
}







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




