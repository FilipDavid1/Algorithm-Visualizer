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
            sortedIndices = emptySet(),
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
            _state.update { it.copy(isSorting = true, sortedIndices = emptySet()) }
            when (SortingAlgorithm.fromString(state.value.selectedAlgorithm)) {
                is SortingAlgorithm.BubbleSort -> bubbleSort()
                is SortingAlgorithm.SelectionSort -> selectionSort()
                is SortingAlgorithm.InsertionSort -> insertionSort()
                is SortingAlgorithm.MergeSort -> mergeSort()
                is SortingAlgorithm.QuickSort -> quickSort()
                is SortingAlgorithm.HeapSort -> heapSort()
            }
            _state.update { it.copy(
                isSorting = false,
                highlightedIndices = emptySet(),
                sortedIndices = (0 until state.value.data.size).toSet()
            )}
        }
    }

    private fun stopSorting() {
        sortingJob?.cancel()
        sortingJob = null
        _state.update { it.copy(isSorting = false, highlightedIndices = emptySet()) }
    }

    private fun resetData() {
        stopSorting()
        _state.update { it.copy(
            data = initialData,
            sortedIndices = emptySet()
        )}
    }

    private suspend fun bubbleSort() {
        val data = state.value.data.toMutableList()
        val n = data.size
        val sortedIndices = mutableSetOf<Int>()
        
        for (i in 0 until n) {
            var swapped = false
            for (j in 0 until n - i - 1) {
                if (!state.value.isSorting) return
                
                _state.update { it.copy(highlightedIndices = setOf(j, j + 1)) }
                delay(500)

                if (data[j] > data[j + 1]) {
                    val temp = data[j]
                    data[j] = data[j + 1]
                    data[j + 1] = temp
                    swapped = true
                    _state.update { it.copy(data = data.toList()) }
                }
            }
            sortedIndices.add(n - i - 1)
            _state.update { it.copy(sortedIndices = sortedIndices.toSet()) }
            if (!swapped) break
        }
    }

    private suspend fun selectionSort() {
        val data = state.value.data.toMutableList()
        val n = data.size
        val sortedIndices = mutableSetOf<Int>()

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
            sortedIndices.add(i)
            _state.update { it.copy(
                data = data.toList(),
                sortedIndices = sortedIndices.toSet()
            )}
        }
        sortedIndices.add(n - 1)
        _state.update { it.copy(sortedIndices = sortedIndices.toSet()) }
    }

    private suspend fun insertionSort() {
        val data = state.value.data.toMutableList()
        val n = data.size
        val sortedIndices = mutableSetOf<Int>()

        for (i in 1 until n) {
            val key = data[i]
            var j = i - 1

            while (j >= 0 && data[j] > key) {
                if (!state.value.isSorting) return

                _state.update { it.copy(highlightedIndices = setOf(j, j + 1)) }
                delay(1000)

                data[j + 1] = data[j]
                j--
                _state.update { it.copy(data = data.toList()) }
            }
            data[j + 1] = key
            for (k in 0..i) {
                sortedIndices.add(k)
            }
            _state.update { it.copy(
                data = data.toList(),
                sortedIndices = sortedIndices.toSet()
            )}
        }
    }

    private suspend fun mergeSort() {
        val data = state.value.data.toMutableList()
        val sortedRanges = mutableSetOf<Int>()
        
        suspend fun merge(left: Int, mid: Int, right: Int) {
            val leftArray = data.subList(left, mid + 1).toMutableList()
            val rightArray = data.subList(mid + 1, right + 1).toMutableList()
            
            var i = 0
            var j = 0
            var k = left
            
            while (i < leftArray.size && j < rightArray.size) {
                if (!state.value.isSorting) return
                
                _state.update { it.copy(highlightedIndices = setOf(k, left + i, mid + 1 + j)) }
                delay(1000)
                
                if (leftArray[i] <= rightArray[j]) {
                    data[k] = leftArray[i]
                    i++
                } else {
                    data[k] = rightArray[j]
                    j++
                }
                k++
                _state.update { it.copy(data = data.toList()) }
            }
            
            while (i < leftArray.size) {
                if (!state.value.isSorting) return
                data[k] = leftArray[i]
                i++
                k++
                _state.update { it.copy(data = data.toList()) }
            }
            
            while (j < rightArray.size) {
                if (!state.value.isSorting) return
                data[k] = rightArray[j]
                j++
                k++
                _state.update { it.copy(data = data.toList()) }
            }

            for (idx in left..right) {
                sortedRanges.add(idx)
            }
            _state.update { it.copy(sortedIndices = sortedRanges.toSet()) }
        }
        
        suspend fun mergeSortRecursive(left: Int, right: Int) {
            if (left < right) {
                val mid = (left + right) / 2
                mergeSortRecursive(left, mid)
                mergeSortRecursive(mid + 1, right)
                merge(left, mid, right)
            } else if (left == right) {
                sortedRanges.add(left)
                _state.update { it.copy(sortedIndices = sortedRanges.toSet()) }
            }
        }
        
        mergeSortRecursive(0, data.size - 1)
    }

    private suspend fun quickSort() {
        // TODO: Implement quick sort
    }

    private suspend fun heapSort() {
        // TODO: Implement heap sort
    }
}