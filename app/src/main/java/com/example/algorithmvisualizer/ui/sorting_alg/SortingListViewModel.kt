package com.example.algorithmvisualizer.ui.sorting_alg

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorithmvisualizer.R
import com.example.algorithmvisualizer.data.ArrayRepository
import com.example.algorithmvisualizer.ui.sorting_alg.model.SortingAlgorithm
import com.example.algorithmvisualizer.ui.sorting_alg.model.SortingEvent
import com.example.algorithmvisualizer.ui.sorting_alg.model.SortingState
import com.example.algorithmvisualizer.ui.sorting_alg.model.StepState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SortingListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ArrayRepository()
    private val initialData = listOf(8, 3, 5, 4, 7, 1, 6, 2)
    private var startTime: Long = 0
    private var timerJob: Job? = null
    private var currentStepIndex = 0

    private val _algorithms = MutableStateFlow(
        listOf(
            getApplication<Application>().getString(R.string.bubble_sort),
            getApplication<Application>().getString(R.string.selection_sort),
            getApplication<Application>().getString(R.string.insertion_sort),
        )
    )
    val algorithms: StateFlow<List<String>> = _algorithms.asStateFlow()

    private val _state = MutableStateFlow(
        SortingState(
            data = initialData,
            highlightedIndices = emptySet(),
            sortedIndices = emptySet(),
            isSorting = false,
            selectedAlgorithm = getApplication<Application>().getString(R.string.bubble_sort)
        )
    )
    val state: StateFlow<SortingState> = _state.asStateFlow()

    private var sortingJob: Job? = null

    init {
        loadRandomArray()
    }

    private fun loadRandomArray() {
        viewModelScope.launch {
            try {
                repository.getRandomArray().fold(
                    onSuccess = { arrayData ->
                        _state.update { it.copy(data = arrayData.numbers) }
                    },
                    onFailure = { 
                        _state.update { it.copy(data = initialData) }
                    }
                )
            } catch (e: Exception) {
                _state.update { it.copy(data = initialData) }
            }
        }
    }

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
            is SortingEvent.ToggleStepMode -> {
                _state.update { it.copy(
                    isStepMode = !it.isStepMode,
                    currentStep = 0,
                    totalSteps = 0,
                    stepHistory = emptyList(),
                    canStepForward = false,
                    canStepBackward = false
                )}
            }
            is SortingEvent.StepForward -> {
                if (currentStepIndex < state.value.stepHistory.lastIndex) {
                    currentStepIndex++
                    applyStep(currentStepIndex)
                }
            }
            is SortingEvent.StepBackward -> {
                if (currentStepIndex > 0) {
                    currentStepIndex--
                    applyStep(currentStepIndex)
                }
            }
            is SortingEvent.LoadNewArray -> {
                loadRandomArray()
            }
            is SortingEvent.SetSpeed -> {
                _state.update { it.copy(sortingSpeed = event.speed) }
            }
        }
    }

    private fun applyStep(index: Int) {
        val step = state.value.stepHistory[index]
        _state.update { it.copy(
            data = step.data,
            highlightedIndices = step.highlightedIndices,
            sortedIndices = step.sortedIndices,
            comparisonMessage = step.comparisonMessage,
            currentStep = index + 1,
            canStepForward = index < state.value.stepHistory.lastIndex,
            canStepBackward = index > 0
        )}
    }

    private fun addStep(
        data: List<Int>,
        highlightedIndices: Set<Int>,
        sortedIndices: Set<Int>,
        comparisonMessage: String
    ) {
        if (!state.value.isStepMode) return

        val step = StepState(
            data = data,
            highlightedIndices = highlightedIndices,
            sortedIndices = sortedIndices,
            comparisonMessage = comparisonMessage
        )

        _state.update { currentState ->
            currentState.copy(
                stepHistory = currentState.stepHistory + step,
                totalSteps = currentState.stepHistory.size + 1
            )
        }
    }

    private fun startSorting() {
        sortingJob?.cancel()
        timerJob?.cancel()
        currentStepIndex = 0

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
            _state.update { it.copy(
                isSorting = true,
                sortedIndices = emptySet(),
                stepHistory = if (it.isStepMode) emptyList() else it.stepHistory
            )}

            when (SortingAlgorithm.fromString(state.value.selectedAlgorithm)) {
                is SortingAlgorithm.BubbleSort -> bubbleSort()
                is SortingAlgorithm.SelectionSort -> selectionSort()
                is SortingAlgorithm.InsertionSort -> insertionSort()
            }

            val endTime = System.currentTimeMillis()
            timerJob?.cancel()

            if (!state.value.isStepMode) {
                _state.update { it.copy(
                    isSorting = false,
                    highlightedIndices = emptySet(),
                    sortedIndices = (0 until state.value.data.size).toSet(),
                    elapsedTimeMs = endTime - startTime
                )}
            }
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
        )}
    }

    private fun resetData() {
        stopSorting()
        currentStepIndex = 0
        _state.update { it.copy(
            sortedIndices = emptySet(),
            comparisonMessage = "",
            elapsedTimeMs = 0,
            stepHistory = emptyList(),
            totalSteps = 0,
            highlightedIndices = emptySet(),
            isSorting = false,
            currentStep = 0,
            canStepForward = false,
            canStepBackward = false,
        )}
        loadRandomArray()
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
                    comparisonMessage = getApplication<Application>().getString(R.string.comparing_elements, data[j], data[j + 1])
                )}
                addStep(data.toList(), setOf(j, j + 1), sortedIndices.toSet(), state.value.comparisonMessage)
                if (!state.value.isStepMode) delay((500L / state.value.sortingSpeed))

                if (data[j] > data[j + 1]) {
                    val temp = data[j]
                    data[j] = data[j + 1]
                    data[j + 1] = temp
                    swapped = true
                    _state.update { it.copy(
                        data = data.toList(),
                        comparisonMessage = getApplication<Application>().getString(R.string.swapped_elements, data[j], data[j + 1])
                    )}
                    addStep(data.toList(), setOf(j, j + 1), sortedIndices.toSet(), state.value.comparisonMessage)
                    if (!state.value.isStepMode) delay((500L / state.value.sortingSpeed))
                }
            }
            sortedIndices.add(n - i - 1)
            _state.update { it.copy(
                sortedIndices = sortedIndices.toSet(),
                comparisonMessage = if (i < n - 1) 
                    getApplication<Application>().getString(R.string.pass_completed, i + 1) 
                else 
                    getApplication<Application>().getString(R.string.sorting_completed)
            )}
            addStep(data.toList(), emptySet(), sortedIndices.toSet(), state.value.comparisonMessage)
            if (!state.value.isStepMode) delay((500L / state.value.sortingSpeed))
            if (!swapped) break
        }

        if (state.value.isStepMode) {
            applyStep(0)
            _state.update { it.copy(
                isSorting = false,
                canStepForward = true,
                canStepBackward = false
            )}
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
                    comparisonMessage = getApplication<Application>().getString(R.string.comparing_elements, data[minIdx], data[j])
                )}
                addStep(data.toList(), setOf(minIdx, j), sortedIndices.toSet(), state.value.comparisonMessage)
                if (!state.value.isStepMode) delay((500L / state.value.sortingSpeed))

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
                comparisonMessage = getApplication<Application>().getString(R.string.moved_smallest_element, data[i], i)
            )}
            addStep(data.toList(), setOf(i, minIdx), sortedIndices.toSet(), state.value.comparisonMessage)
            if (!state.value.isStepMode) delay((500L / state.value.sortingSpeed))
        }
        
        sortedIndices.add(n - 1)
        _state.update { it.copy(
            sortedIndices = sortedIndices.toSet(),
            comparisonMessage = getApplication<Application>().getString(R.string.sorting_completed)
        )}
        addStep(data.toList(), emptySet(), sortedIndices.toSet(), state.value.comparisonMessage)

        if (state.value.isStepMode) {
            applyStep(0)
            _state.update { it.copy(
                isSorting = false,
                canStepForward = true,
                canStepBackward = false
            )}
        }
    }

    private suspend fun insertionSort() {
        val data = state.value.data.toMutableList()
        val n = data.size
        val sortedIndices = mutableSetOf<Int>()
        sortedIndices.add(0)

        for (i in 1 until n) {
            val key = data[i]
            var j = i - 1

            _state.update { it.copy(
                highlightedIndices = setOf(i),
                comparisonMessage = getApplication<Application>().getString(R.string.inserting_element, key)
            )}
            addStep(data.toList(), setOf(i), sortedIndices.toSet(), state.value.comparisonMessage)
            if (!state.value.isStepMode) delay((500L / state.value.sortingSpeed))

            while (j >= 0 && data[j] > key) {
                if (!state.value.isSorting) return

                _state.update { it.copy(
                    highlightedIndices = setOf(j, j + 1),
                    comparisonMessage = getApplication<Application>().getString(R.string.comparing_elements, data[j], key)
                )}
                addStep(data.toList(), setOf(j, j + 1), sortedIndices.toSet(), state.value.comparisonMessage)
                if (!state.value.isStepMode) delay((500L / state.value.sortingSpeed))

                val temp = data[j + 1]
                data[j + 1] = data[j]
                data[j] = temp

                _state.update { it.copy(
                    data = data.toList(),
                    comparisonMessage = getApplication<Application>().getString(R.string.swapped_elements, data[j], data[j + 1])
                )}
                addStep(data.toList(), setOf(j, j + 1), sortedIndices.toSet(), state.value.comparisonMessage)
                if (!state.value.isStepMode) delay((500L / state.value.sortingSpeed))
                
                j--
            }
            
            for (k in 0..i) {
                sortedIndices.add(k)
            }
            
            _state.update { it.copy(
                data = data.toList(),
                sortedIndices = sortedIndices.toSet(),
                highlightedIndices = setOf(j + 1),
                comparisonMessage = getApplication<Application>().getString(R.string.inserted_element, key)
            )}
            addStep(data.toList(), setOf(j + 1), sortedIndices.toSet(), state.value.comparisonMessage)
            if (!state.value.isStepMode) delay((500L / state.value.sortingSpeed))
        }

        _state.update { it.copy(
            sortedIndices = (0 until n).toSet(),
            highlightedIndices = emptySet(),
            comparisonMessage = getApplication<Application>().getString(R.string.sorting_completed)
        )}
        addStep(data.toList(), emptySet(), (0 until n).toSet(), state.value.comparisonMessage)

        if (state.value.isStepMode) {
            applyStep(0)
            _state.update { it.copy(
                isSorting = false,
                canStepForward = true,
                canStepBackward = false
            )}
        }
    }

}