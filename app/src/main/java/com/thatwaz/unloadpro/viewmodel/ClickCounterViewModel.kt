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
        resetCount()  // Call to reset the count when ViewModel is initialized

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

    fun resetCount() {
        countRef.set(mapOf("count" to 0))  // Reset the count to 0
    }

    fun incrementCount() {
        val currentCount = _count.value ?: 0
        countRef.set(mapOf("count" to currentCount + 1))
    }
}







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


