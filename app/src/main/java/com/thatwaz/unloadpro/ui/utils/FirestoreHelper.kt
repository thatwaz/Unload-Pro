package com.thatwaz.unloadpro.ui.utils


import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await



object FirestoreHelper {

    suspend fun updateCount(firestore: FirebaseFirestore, count: Int) {
        val countRef = firestore.collection("counts").document("clickCount")
        countRef.set(mapOf("count" to count)).await()
    }

    suspend fun updateEstimatedCompletionTime(firestore: FirebaseFirestore, time: String) {
        val estCompletionTimeRef = firestore.collection("completionTimes").document("estimatedCompletionTime")
        estCompletionTimeRef.set(mapOf("time" to time)).await()
    }

    fun getCountRef(firestore: FirebaseFirestore) = firestore.collection("counts").document("clickCount")

    fun getEstimatedCompletionTimeRef(firestore: FirebaseFirestore) = firestore.collection("completionTimes").document("estimatedCompletionTime")
}

