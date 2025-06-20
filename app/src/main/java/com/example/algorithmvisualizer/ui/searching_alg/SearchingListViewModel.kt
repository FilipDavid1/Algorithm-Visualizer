package com.example.algorithmvisualizer.ui.searching_alg

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorithmvisualizer.R
import com.example.algorithmvisualizer.ui.searching_alg.model.SearchingAlgorithm
import com.example.algorithmvisualizer.ui.searching_alg.model.SearchingEvent
import com.example.algorithmvisualizer.ui.searching_alg.model.SearchingState
import com.example.algorithmvisualizer.ui.searching_alg.model.SearchStep
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchingListViewModel(application: Application) : AndroidViewModel(application) {
    private val initialData = listOf(1, 2, 3, 4, 5, 6, 7, 8)
    private var startTime: Long = 0
    private var timerJob: Job? = null
    private var stepHistory = mutableListOf<SearchStep>()
    private var currentStepIndex = -1

    private val _algorithms = MutableStateFlow(
        listOf(
            getApplication<Application>().getString(R.string.linear_search),
            getApplication<Application>().getString(R.string.binary_search),
            getApplication<Application>().getString(R.string.jump_search),
        )
    )
    val algorithms: StateFlow<List<String>> = _algorithms.asStateFlow()

    private val _state = MutableStateFlow(
        SearchingState(
            data = initialData,
            selectedAlgorithm = getApplication<Application>().getString(R.string.linear_search)
        )
    )
    val state: StateFlow<SearchingState> = _state.asStateFlow()

    private var searchingJob: Job? = null

    fun onEvent(event: SearchingEvent) {
        when (event) {
            is SearchingEvent.SelectAlgorithm -> {
                _state.update { it.copy(selectedAlgorithm = event.algorithm) }
            }
            is SearchingEvent.SetTargetValue -> {
                _state.update { it.copy(targetValue = event.value) }
            }
            is SearchingEvent.StartSearching -> {
                if (!state.value.isSearching && state.value.targetValue != null) {
                    startSearching()
                }
            }
            is SearchingEvent.StopSearching -> {
                stopSearching()
            }
            is SearchingEvent.ResetData -> {
                resetData()
            }
            is SearchingEvent.ToggleStepMode -> {
                _state.update { it.copy(isStepMode = !it.isStepMode) }
            }
            is SearchingEvent.StepForward -> {
                stepForward()
            }
            is SearchingEvent.StepBackward -> {
                stepBackward()
            }
        }
    }

    private fun stepForward() {
        if (currentStepIndex < stepHistory.size - 1) {
            currentStepIndex++
            applyStep(stepHistory[currentStepIndex])
        }
    }

    private fun stepBackward() {
        if (currentStepIndex > 0) {
            currentStepIndex--
            applyStep(stepHistory[currentStepIndex])
        }
    }

    private fun applyStep(step: SearchStep) {
        _state.update { it.copy(
            highlightedIndices = step.highlightedIndices,
            comparisonMessage = step.comparisonMessage,
            foundIndex = step.foundIndex,
            canStepForward = currentStepIndex < stepHistory.size - 1,
            canStepBackward = currentStepIndex > 0,
            currentStep = currentStepIndex + 1,
            totalSteps = stepHistory.size
        )}
    }

    private fun addSearchStep(highlightedIndices: Set<Int>, comparisonMessage: String, foundIndex: Int? = null) {
        if (state.value.isStepMode) {
            stepHistory.add(SearchStep(highlightedIndices, comparisonMessage, foundIndex))
            _state.update { it.copy(
                totalSteps = stepHistory.size,
                canStepForward = currentStepIndex < stepHistory.size - 1
            )}
        }
    }

    private fun startSearching() {
        searchingJob?.cancel()
        timerJob?.cancel()

        startTime = System.currentTimeMillis()
        stepHistory.clear()
        currentStepIndex = -1

        if (!state.value.isStepMode) {
            timerJob = viewModelScope.launch {
                while (true) {
                    _state.update { it.copy(
                        elapsedTimeMs = System.currentTimeMillis() - startTime
                    )}
                    delay(16)
                }
            }
        }

        searchingJob = viewModelScope.launch {
            _state.update { it.copy(
                isSearching = true,
                foundIndex = null,
                highlightedIndices = emptySet(),
                currentStep = 0,
                totalSteps = 0,
                canStepForward = false,
                canStepBackward = false
            )}

            when (SearchingAlgorithm.fromString(state.value.selectedAlgorithm)) {
                is SearchingAlgorithm.LinearSearch -> linearSearch()
                is SearchingAlgorithm.BinarySearch -> binarySearch()
                is SearchingAlgorithm.JumpSearch -> jumpSearch()
            }
        }
    }

    private fun stopSearching() {
        searchingJob?.cancel()
        timerJob?.cancel()
        searchingJob = null
        timerJob = null
        _state.update { it.copy(
            isSearching = false,
            highlightedIndices = emptySet(),
            elapsedTimeMs = System.currentTimeMillis() - startTime
        )}
    }

    private fun resetData() {
        stopSearching()
        stepHistory.clear()
        currentStepIndex = -1
        _state.update { it.copy(
            data = initialData,
            foundIndex = null,
            targetValue = null,
            comparisonMessage = "",
            elapsedTimeMs = 0,
            currentStep = 0,
            totalSteps = 0,
            canStepForward = false,
            canStepBackward = false
        )}
    }

    private suspend fun linearSearch() {
        val targetValue = state.value.targetValue ?: return
        val data = state.value.data
        
        if (state.value.isStepMode) {
            stepHistory.clear()
            currentStepIndex = -1
        }

        for (i in data.indices) {
            if (!state.value.isSearching) return

            val message = getApplication<Application>().getString(
                R.string.comparing_with_target,
                data[i],
                targetValue
            )
            
            if (state.value.isStepMode) {
                addSearchStep(setOf(i), message)
            } else {
                _state.update { it.copy(
                    highlightedIndices = setOf(i),
                    comparisonMessage = message
                )}
                delay(500)
            }

            if (data[i] == targetValue) {
                val foundMessage = getApplication<Application>().getString(
                    R.string.found_target_at_index,
                    targetValue,
                    i
                )
                
                if (state.value.isStepMode) {
                    addSearchStep(setOf(i), foundMessage, i)
                    _state.update { it.copy(
                        isSearching = false,
                        currentStep = 0,
                        canStepForward = true,
                        comparisonMessage = getApplication<Application>().getString(R.string.step_mode_ready)
                    )}
                    return
                } else {
                    timerJob?.cancel()
                    _state.update { it.copy(
                        foundIndex = i,
                        isSearching = false,
                        comparisonMessage = foundMessage
                    )}
                }
                return
            }
        }

        val notFoundMessage = getApplication<Application>().getString(
            R.string.target_not_found,
            targetValue
        )
        
        if (state.value.isStepMode) {
            addSearchStep(emptySet(), notFoundMessage)
            _state.update { it.copy(
                isSearching = false,
                currentStep = 0,
                canStepForward = true,
                comparisonMessage = getApplication<Application>().getString(R.string.step_mode_ready)
            )}
        } else {
            timerJob?.cancel()
            _state.update { it.copy(
                isSearching = false,
                comparisonMessage = notFoundMessage
            )}
        }
    }


    private suspend fun binarySearch() {
        val targetValue = state.value.targetValue ?: return
        val data = state.value.data
        var left = 0
        var right = data.size - 1

        if (state.value.isStepMode) {
            stepHistory.clear()
            currentStepIndex = -1
        }

        while (left <= right) {
            if (!state.value.isSearching) return

            val mid = (left + right) / 2
            val message = getApplication<Application>().getString(
                R.string.comparing_with_target,
                data[mid],
                targetValue
            )

            if (state.value.isStepMode) {
                addSearchStep(setOf(mid), message)
            } else {
                _state.update { it.copy(
                    highlightedIndices = setOf(mid),
                    comparisonMessage = message
                )}
                delay(500)
            }

            when {
                data[mid] == targetValue -> {
                    val foundMessage = getApplication<Application>().getString(
                        R.string.found_target_at_index,
                        targetValue,
                        mid
                    )

                    if (state.value.isStepMode) {
                        addSearchStep(setOf(mid), foundMessage, mid)
                        _state.update { it.copy(
                            isSearching = false,
                            currentStep = 1,
                            canStepForward = true
                        )}
                    } else {
                        timerJob?.cancel()
                        _state.update { it.copy(
                            foundIndex = mid,
                            isSearching = false,
                            comparisonMessage = foundMessage
                        )}
                    }
                    return
                }
                data[mid] < targetValue -> {
                    left = mid + 1
                    val searchMessage = getApplication<Application>().getString(
                        R.string.searching_right_half
                    )
                    if (state.value.isStepMode) {
                        addSearchStep(setOf(mid), searchMessage)
                    } else {
                        _state.update { it.copy(comparisonMessage = searchMessage) }
                    }
                }
                else -> {
                    right = mid - 1
                    val searchMessage = getApplication<Application>().getString(
                        R.string.searching_left_half
                    )
                    if (state.value.isStepMode) {
                        addSearchStep(setOf(mid), searchMessage)
                    } else {
                        _state.update { it.copy(comparisonMessage = searchMessage) }
                    }
                }
            }
        }

        val notFoundMessage = getApplication<Application>().getString(
            R.string.target_not_found,
            targetValue
        )

        if (state.value.isStepMode) {
            addSearchStep(emptySet(), notFoundMessage)
            _state.update { it.copy(
                isSearching = false,
                currentStep = 1,
                canStepForward = true
            )}
        } else {
            timerJob?.cancel()
            _state.update { it.copy(
                isSearching = false,
                comparisonMessage = notFoundMessage
            )}
        }
    }

    private suspend fun jumpSearch() {
        val targetValue = state.value.targetValue ?: return
        val data = state.value.data
        val n = data.size
        var step = kotlin.math.sqrt(n.toDouble()).toInt()
        var prev = 0

        if (state.value.isStepMode) {
            stepHistory.clear()
            currentStepIndex = -1
        }

        while (data[kotlin.math.min(step, n) - 1] < targetValue) {
            if (!state.value.isSearching) return

            prev = step
            step += kotlin.math.sqrt(n.toDouble()).toInt()
            
            val message = getApplication<Application>().getString(
                R.string.jumping_to_block,
                prev
            )

            if (state.value.isStepMode) {
                addSearchStep(setOf(prev), message)
            } else {
                _state.update { it.copy(
                    highlightedIndices = setOf(prev),
                    comparisonMessage = message
                )}
                delay(500)
            }

            if (prev >= n) {
                val notFoundMessage = getApplication<Application>().getString(
                    R.string.target_not_found,
                    targetValue
                )

                if (state.value.isStepMode) {
                    addSearchStep(emptySet(), notFoundMessage)
                    _state.update { it.copy(
                        isSearching = false,
                        currentStep = 1,
                        canStepForward = true
                    )}
                } else {
                    timerJob?.cancel()
                    _state.update { it.copy(
                        isSearching = false,
                        comparisonMessage = notFoundMessage
                    )}
                }
                return
            }
        }

        while (data[prev] < targetValue) {
            if (!state.value.isSearching) return

            prev++
            val message = getApplication<Application>().getString(
                R.string.linear_scanning,
                prev
            )

            if (state.value.isStepMode) {
                addSearchStep(setOf(prev), message)
            } else {
                _state.update { it.copy(
                    highlightedIndices = setOf(prev),
                    comparisonMessage = message
                )}
                delay(500)
            }

            if (prev == kotlin.math.min(step, n)) {
                val notFoundMessage = getApplication<Application>().getString(
                    R.string.target_not_found,
                    targetValue
                )

                if (state.value.isStepMode) {
                    addSearchStep(emptySet(), notFoundMessage)
                    _state.update { it.copy(
                        isSearching = false,
                        currentStep = 1,
                        canStepForward = true
                    )}
                } else {
                    timerJob?.cancel()
                    _state.update { it.copy(
                        isSearching = false,
                        comparisonMessage = notFoundMessage
                    )}
                }
                return
            }
        }

        if (data[prev] == targetValue) {
            val foundMessage = getApplication<Application>().getString(
                R.string.found_target_at_index,
                targetValue,
                prev
            )

            if (state.value.isStepMode) {
                addSearchStep(setOf(prev), foundMessage, prev)
                _state.update { it.copy(
                    isSearching = false,
                    currentStep = 1,
                    canStepForward = true
                )}
            } else {
                timerJob?.cancel()
                _state.update { it.copy(
                    foundIndex = prev,
                    isSearching = false,
                    comparisonMessage = foundMessage
                )}
            }
            return
        }

        val notFoundMessage = getApplication<Application>().getString(
            R.string.target_not_found,
            targetValue
        )

        if (state.value.isStepMode) {
            addSearchStep(emptySet(), notFoundMessage)
            _state.update { it.copy(
                isSearching = false,
                currentStep = 1,
                canStepForward = true
            )}
        } else {
            timerJob?.cancel()
            _state.update { it.copy(
                isSearching = false,
                comparisonMessage = notFoundMessage
            )}
        }
    }

    override fun onCleared() {
        super.onCleared()
        searchingJob?.cancel()
        timerJob?.cancel()
    }
} 