package com.thatwaz.unloadpro.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ClickCounterViewModel @Inject constructor(
    private val firestore: FirebaseFirestore // Injected instance of FirebaseFirestore
) : ViewModel() {
    private val countRef = firestore.collection("counts").document("clickCount") // Use the injected instance
    private val _count = MutableLiveData<Int>()
    val count: LiveData<Int> = _count

    init {
        resetCount(150)  // Call to reset the count when ViewModel is initialized

        val listenerRegistration = countRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("Firestore", "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val newCount = snapshot.getLong("count")?.toInt() ?: 0
                _count.value = newCount
                Log.d("Firestore", "Count updated to: $newCount")
            } else {
                Log.d("Firestore", "Current data: null")
            }
        }
    }

    fun resetCount(initialCartonCount: Int) {
        countRef.set(mapOf("count" to initialCartonCount))  // Set the count to initialCartonCount
        _count.value = initialCartonCount
    }

    fun decrementCount() {
        val currentCount = _count.value ?: 0
        countRef.set(mapOf("count" to currentCount - 100))
    }
}


//@HiltViewModel
//class ClickCounterViewModel @Inject constructor(
//    private val firestore: FirebaseFirestore // Injected instance of FirebaseFirestore
//) : ViewModel() {
//    private val countRef = firestore.collection("counts").document("clickCount") // Use the injected instance
//    private val _count = MutableLiveData<Int>()
//    val count: LiveData<Int> = _count
//
//    init {
//        resetCount()  // Call to reset the count when ViewModel is initialized
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
//    fun resetCount() {
//        countRef.set(mapOf("count" to 0))  // Reset the count to 0
//    }
//
//    fun decrementCount() {
//        val currentCount = _count.value ?: 0
//        countRef.set(mapOf("count" to currentCount -100))
//    }
//}


//@HiltViewModel
//class ClickCounterViewModel @Inject constructor(
//    private val firestore: FirebaseFirestore, // Injected instance of FirebaseFirestore
//    private val savedStateHandle: SavedStateHandle // Assuming initial count can be passed via Navigation arguments
//) : ViewModel() {
//    private val countRef = firestore.collection("counts").document("clickCount") // Use the injected instance
////    private var _count = mutableStateOf(0)  // Using MutableState for compose
////    val count: State<Int> = _count
//
//    private val _count = MutableLiveData<Int>()
//    val count: LiveData<Int> = _count
//
//
//    init {
//        // Initialize count from saved state or reset if not available
//        _count.value = savedStateHandle.get<Int>("initialCount") ?: 0
//
//        // Set the initial count in Firestore if it's newly provided
//        if (savedStateHandle.contains("initialCount")) {
//            countRef.set(mapOf("count" to _count.value))
//        }
//
//        // Listen for changes in the Firestore document
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
//    fun decrementCount() {
//        val currentCount = _count.value ?: 0
//        if (currentCount > 100) { // Ensure it does not go below zero
//            countRef.set(mapOf("count" to currentCount - 100))
//        }
//    }
//}




//@HiltViewModel
//class ClickCounterViewModel @Inject constructor(
//    private val firestore: FirebaseFirestore // Injected instance of FirebaseFirestore
//) : ViewModel() {
//    private val countRef = firestore.collection("counts").document("clickCount") // Use the injected instance
//    private val _count = MutableLiveData<Int>()
//    val count: LiveData<Int> = _count
//
//    init {
//        resetCount()  // Call to reset the count when ViewModel is initialized
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
//    fun resetCount() {
//        countRef.set(mapOf("count" to 0))  // Reset the count to 0
//    }
//
//    // Renaming to reflect the new functionality
//    fun decrementCount() {
//        val currentCount = _count.value ?: 0
//        countRef.set(mapOf("count" to currentCount - 100))
//    }
//}








//class ClickCounterViewModel @Inject constructor(
//    private val firestore: FirebaseFirestore // Injected instance of FirebaseFirestore
//) : ViewModel() {
//    private val countRef = firestore.collection("counts").document("clickCount") // Use the injected instance
//    private val _count = MutableLiveData<Int>()
//    val count: LiveData<Int> = _count
//
//    init {
//        val listenerRegistration = countRef.addSnapshotListener { snapshot, e ->
//            if (e != null) {
//                if (e.code == FirebaseFirestoreException.Code.UNAVAILABLE) {
//                    Log.w("Firestore", "Attempting to reconnect...", e)
//                    // Optional: Implement a retry mechanism
//                } else {
//                    Log.w("Firestore", "Listen failed.", e)
//                }
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
//
//        // Consider adding a mechanism to remove and re-add the listener when the app detects a network change.
//    }
//
//
//    fun incrementCount() {
//        val currentCount = _count.value ?: 0
//        // Increment the current count and update Firestore
//        countRef.set(mapOf("count" to currentCount + 1))
//    }
//}


