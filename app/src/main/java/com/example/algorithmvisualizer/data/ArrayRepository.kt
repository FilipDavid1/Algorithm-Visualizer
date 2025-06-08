package com.example.algorithmvisualizer.data

class ArrayRepository {
    private val firebaseService = FirebaseService()

    suspend fun addArray(numbers: List<Int>): Result<Unit> {
        return firebaseService.addArray(numbers)
    }

    suspend fun getRandomArray(): Result<ArrayData> {
        return firebaseService.getRandomArray()
    }
} 