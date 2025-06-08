package com.example.algorithmvisualizer.data

data class ArrayData(
    val id: String = "",
    val numbers: List<Int> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
) 