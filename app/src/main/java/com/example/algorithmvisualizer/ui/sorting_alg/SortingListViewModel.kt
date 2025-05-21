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
    private var startTime: Long = 0
    private var timerJob: Job? = null

    private val _algorithms = MutableStateFlow(
        listOf(
            "Bubble Sort",
            "Selection Sort",
            "Insertion Sort",
            "Merge Sort"
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

            is SortingEvent.SetAnimationSpeed -> TODO()
        }
    }

    private fun startSorting() {
        sortingJob?.cancel()
        timerJob?.cancel()

        startTime = System.currentTimeMillis()

        // Start timer updates
        timerJob = viewModelScope.launch {
            while (true) {
                _state.update { it.copy(
                    elapsedTimeMs = System.currentTimeMillis() - startTime
                )}
                delay(16) // Update approximately 60 times per second
            }
        }

        sortingJob = viewModelScope.launch {
            _state.update { it.copy(isSorting = true, sortedIndices = emptySet()) }
            when (SortingAlgorithm.fromString(state.value.selectedAlgorithm)) {
                is SortingAlgorithm.BubbleSort -> bubbleSort()
                is SortingAlgorithm.SelectionSort -> selectionSort()
                is SortingAlgorithm.InsertionSort -> insertionSort()
                is SortingAlgorithm.MergeSort -> mergeSort()
            }
            val endTime = System.currentTimeMillis()
            timerJob?.cancel() // Stop timer updates
            _state.update { it.copy(
                isSorting = false,
                highlightedIndices = emptySet(),
                sortedIndices = (0 until state.value.data.size).toSet(),
                elapsedTimeMs = endTime - startTime
            )}
        }
    }

    private fun stopSorting() {
        sortingJob?.cancel()
        timerJob?.cancel()
        sortingJob = null
        timerJob = null
        _state.update { it.copy(
            isSorting = false,
            highlightedIndices = emptySet(),
            elapsedTimeMs = System.currentTimeMillis() - startTime
        ) }
    }

    private fun resetData() {
        stopSorting()
        _state.update { it.copy(
            data = initialData,
            sortedIndices = emptySet(),
            comparisonMessage = "",
            elapsedTimeMs = 0
        )}
    }

    override fun onCleared() {
        super.onCleared()
        sortingJob?.cancel()
        timerJob?.cancel()
    }

    private suspend fun bubbleSort() {
        val data = state.value.data.toMutableList()
        val n = data.size
        val sortedIndices = mutableSetOf<Int>()

        for (i in 0 until n) {
            var swapped = false
            for (j in 0 until n - i - 1) {
                if (!state.value.isSorting) return

                _state.update { it.copy(
                    highlightedIndices = setOf(j, j + 1),
                    comparisonMessage = "Comparing elements ${data[j]} and ${data[j + 1]}"
                ) }
                delay(500)

                if (data[j] > data[j + 1]) {
                    val temp = data[j]
                    data[j] = data[j + 1]
                    data[j + 1] = temp
                    swapped = true
                    _state.update { it.copy(
                        data = data.toList(),
                        comparisonMessage = "Swapped elements ${data[j]} and ${data[j + 1]}"
                    ) }
                }
            }
            sortedIndices.add(n - i - 1)
            _state.update { it.copy(
                sortedIndices = sortedIndices.toSet(),
                comparisonMessage = if (i < n - 1) "Pass ${i + 1} completed" else "Sorting completed!"
            ) }
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

                _state.update { it.copy(
                    highlightedIndices = setOf(minIdx, j),
                    comparisonMessage = "Comparing elements ${data[minIdx]} and ${data[j]}"
                ) }
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
                sortedIndices = sortedIndices.toSet(),
                comparisonMessage = "Moved smallest element ${data[i]} to position $i"
            )}
        }
        sortedIndices.add(n - 1)
        _state.update { it.copy(
            sortedIndices = sortedIndices.toSet(),
            comparisonMessage = "Sorting completed!"
        ) }
    }

    private suspend fun insertionSort() {
        val data = state.value.data.toMutableList()
        val n = data.size
        val sortedIndices = mutableSetOf<Int>()

        for (i in 1 until n) {
            val key = data[i]
            var j = i - 1

            _state.update { it.copy(
                comparisonMessage = "Inserting element $key into sorted portion"
            )}

            while (j >= 0 && data[j] > key) {
                if (!state.value.isSorting) return

                _state.update { it.copy(
                    highlightedIndices = setOf(j, j + 1),
                    comparisonMessage = "Comparing elements ${data[j]} and $key"
                ) }
                delay(1000)

                data[j + 1] = data[j]
                j--
                _state.update { it.copy(
                    data = data.toList(),
                    comparisonMessage = "Shifted element ${data[j + 2]} right"
                ) }
            }
            data[j + 1] = key
            for (k in 0..i) {
                sortedIndices.add(k)
            }
            _state.update { it.copy(
                data = data.toList(),
                sortedIndices = sortedIndices.toSet(),
                comparisonMessage = "Inserted $key at correct position"
            )}
        }
    }

    private suspend fun mergeSort() {
        val data = state.value.data.toMutableList()
        val sortedRanges = mutableSetOf<Int>()

        suspend fun merge(left: Int, mid: Int, right: Int) {
            _state.update { it.copy(
                comparisonMessage = "Merging subarrays from index $left to $right"
            )}

            val leftArray = data.subList(left, mid + 1).toMutableList()
            val rightArray = data.subList(mid + 1, right + 1).toMutableList()

            var i = 0
            var j = 0
            var k = left

            while (i < leftArray.size && j < rightArray.size) {
                if (!state.value.isSorting) return

                _state.update { it.copy(
                    highlightedIndices = setOf(k, left + i, mid + 1 + j),
                    comparisonMessage = "Comparing elements ${leftArray[i]} and ${rightArray[j]}"
                ) }
                delay(1000)

                if (leftArray[i] <= rightArray[j]) {
                    data[k] = leftArray[i]
                    i++
                } else {
                    data[k] = rightArray[j]
                    j++
                }
                k++
                _state.update { it.copy(
                    data = data.toList(),
                    comparisonMessage = "Placed smaller element at position $k"
                ) }
            }

            while (i < leftArray.size) {
                if (!state.value.isSorting) return
                data[k] = leftArray[i]
                i++
                k++
                _state.update { it.copy(
                    data = data.toList(),
                    comparisonMessage = "Copying remaining elements from left subarray"
                ) }
            }

            while (j < rightArray.size) {
                if (!state.value.isSorting) return
                data[k] = rightArray[j]
                j++
                k++
                _state.update { it.copy(
                    data = data.toList(),
                    comparisonMessage = "Copying remaining elements from right subarray"
                ) }
            }

            for (idx in left..right) {
                sortedRanges.add(idx)
            }
            _state.update { it.copy(
                sortedIndices = sortedRanges.toSet(),
                comparisonMessage = "Merged subarray from $left to $right"
            ) }
        }

        suspend fun mergeSortRecursive(left: Int, right: Int) {
            if (left < right) {
                val mid = (left + right) / 2
                _state.update { it.copy(
                    comparisonMessage = "Dividing array from index $left to $right"
                )}
                mergeSortRecursive(left, mid)
                mergeSortRecursive(mid + 1, right)
                merge(left, mid, right)
            } else if (left == right) {
                sortedRanges.add(left)
                _state.update { it.copy(
                    sortedIndices = sortedRanges.toSet(),
                    comparisonMessage = "Single element at index $left is sorted"
                ) }
            }
        }
        
        mergeSortRecursive(0, data.size - 1)
    }
}