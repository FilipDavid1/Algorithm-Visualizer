package com.example.algorithmvisualizer.ui.sorting_alg

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorithmvisualizer.ui.sorting_alg.model.SortingAlgorithm
import com.example.algorithmvisualizer.ui.sorting_alg.model.SortingEvent
import com.example.algorithmvisualizer.ui.sorting_alg.model.SortingState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SortingListViewModel : ViewModel() {
    private val initialData = listOf(8, 3, 5, 4, 7, 1, 6, 2)
    
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
    val algorithms: StateFlow<List<String>> = _algorithms.asStateFlow()

    private val _state = MutableStateFlow(
        SortingState(
            data = initialData,
            highlightedIndices = emptySet(),
            isSorting = false,
            selectedAlgorithm = "Bubble Sort"
        )
    )
    val state: StateFlow<SortingState> = _state.asStateFlow()

    private var sortingJob: Job? = null

    fun onEvent(event: SortingEvent) {
        when (event) {
            is SortingEvent.SelectAlgorithm -> {
                _state.update { it.copy(selectedAlgorithm = event.algorithm) }
            }
            is SortingEvent.StartSorting -> {
                if (!state.value.isSorting) {
                    startSorting()
                }
            }
            is SortingEvent.StopSorting -> {
                stopSorting()
            }
            is SortingEvent.ResetData -> {
                resetData()
            }
        }
    }

    private fun startSorting() {
        sortingJob?.cancel()
        sortingJob = viewModelScope.launch {
            _state.update { it.copy(isSorting = true) }
            when (SortingAlgorithm.fromString(state.value.selectedAlgorithm)) {
                is SortingAlgorithm.BubbleSort -> bubbleSort()
                is SortingAlgorithm.SelectionSort -> selectionSort()
                is SortingAlgorithm.InsertionSort -> insertionSort()
                is SortingAlgorithm.MergeSort -> mergeSort()
                is SortingAlgorithm.QuickSort -> quickSort()
                is SortingAlgorithm.HeapSort -> heapSort()
            }
            _state.update { it.copy(isSorting = false, highlightedIndices = emptySet()) }
        }
    }

    private fun stopSorting() {
        sortingJob?.cancel()
        sortingJob = null
        _state.update { it.copy(isSorting = false, highlightedIndices = emptySet()) }
    }

    private fun resetData() {
        stopSorting()
        _state.update { it.copy(data = initialData) }
    }

    private suspend fun bubbleSort() {
        val data = state.value.data.toMutableList()
        val n = data.size
        
        for (i in 0 until n) {
            for (j in 0 until n - i - 1) {
                if (!state.value.isSorting) return
                
                _state.update { it.copy(highlightedIndices = setOf(j, j + 1)) }
                delay(500)

                if (data[j] > data[j + 1]) {
                    val temp = data[j]
                    data[j] = data[j + 1]
                    data[j + 1] = temp
                    _state.update { it.copy(data = data.toList()) }
                }
            }
        }
    }

    private suspend fun selectionSort() {
        val data = state.value.data.toMutableList()
        val n = data.size

        for (i in 0 until n - 1) {
            var minIdx = i
            for (j in i + 1 until n) {
                if (!state.value.isSorting) return
                
                _state.update { it.copy(highlightedIndices = setOf(minIdx, j)) }
                delay(500)

                if (data[j] < data[minIdx]) {
                    minIdx = j
                }
            }
            val temp = data[minIdx]
            data[minIdx] = data[i]
            data[i] = temp
            _state.update { it.copy(data = data.toList()) }
        }
    }

    // Placeholder for other sorting algorithms
    private suspend fun insertionSort() {
        // TODO: Implement insertion sort
    }

    private suspend fun mergeSort() {
        // TODO: Implement merge sort
    }

    private suspend fun quickSort() {
        // TODO: Implement quick sort
    }

    private suspend fun heapSort() {
        // TODO: Implement heap sort
    }
}