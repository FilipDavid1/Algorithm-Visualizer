package com.example.algorithmvisualizer.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class FirebaseService {
    private val firestore = FirebaseFirestore.getInstance()
    private val arraysCollection = firestore.collection("arrays")

    suspend fun addArray(numbers: List<Int>): Result<Unit> = try {
        val arrayData = hashMapOf(
            "numbers" to numbers,
            "timestamp" to System.currentTimeMillis()
        )
        arraysCollection.add(arrayData).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getRandomArray(): Result<ArrayData> {
        return try {
            // Get all documents
            val snapshot = arraysCollection
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            if (snapshot.isEmpty) {
                return Result.failure(Exception("No arrays found"))
            }

            // Get a random document
            val randomIndex = (0 until snapshot.size()).random()
            val doc = snapshot.documents[randomIndex]
            
            val numbers = (doc.get("numbers") as? List<*>)?.mapNotNull { 
                when (it) {
                    is Number -> it.toInt()
                    else -> null
                }
            } ?: return Result.failure(Exception("Invalid array data"))

            Result.success(
                ArrayData(
                    id = doc.id,
                    numbers = numbers,
                    timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}