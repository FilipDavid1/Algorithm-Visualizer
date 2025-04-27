package com.example.algorithmvisualizer.ui.sorting_alg

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SortingListViewModel: ViewModel() {
    private val _algorithms = MutableStateFlow(
        listOf(
            "Bubble Sort",
            "Selection Sort",
            "Insertion Sort",
            "Merge Sort",
            "Quick Sort",
            "Heap Sort"
        )
    )

    val algorithms: StateFlow<List<String>> = _algorithms
}