package com.example.algorithmvisualizer.ui.searching_alg

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorithmvisualizer.R
import com.example.algorithmvisualizer.ui.searching_alg.model.SearchingAlgorithm
import com.example.algorithmvisualizer.ui.searching_alg.model.SearchingEvent
import com.example.algorithmvisualizer.ui.searching_alg.model.SearchingState
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
        }
    }

    private fun startSearching() {
        searchingJob?.cancel()
        timerJob?.cancel()

        startTime = System.currentTimeMillis()

        timerJob = viewModelScope.launch {
            while (true) {
                _state.update { it.copy(
                    elapsedTimeMs = System.currentTimeMillis() - startTime
                )}
                delay(16)
            }
        }

        searchingJob = viewModelScope.launch {
            _state.update { it.copy(
                isSearching = true,
                foundIndex = null,
                highlightedIndices = emptySet()
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
        _state.update { it.copy(
            data = initialData,
            foundIndex = null,
            targetValue = null,
            comparisonMessage = "",
            elapsedTimeMs = 0
        )}
    }

    override fun onCleared() {
        super.onCleared()
        searchingJob?.cancel()
        timerJob?.cancel()
    }

    private suspend fun linearSearch() {
        val targetValue = state.value.targetValue ?: return
        val data = state.value.data

        for (i in data.indices) {
            if (!state.value.isSearching) return

            _state.update { it.copy(
                highlightedIndices = setOf(i),
                comparisonMessage = getApplication<Application>().getString(
                    R.string.comparing_with_target,
                    data[i],
                    targetValue
                )
            )}
            delay(500)

            if (data[i] == targetValue) {
                timerJob?.cancel()
                _state.update { it.copy(
                    foundIndex = i,
                    isSearching = false,
                    comparisonMessage = getApplication<Application>().getString(
                        R.string.found_target_at_index,
                        targetValue,
                        i
                    )
                )}
                return
            }
        }

        timerJob?.cancel()
        _state.update { it.copy(
            isSearching = false,
            comparisonMessage = getApplication<Application>().getString(
                R.string.target_not_found,
                targetValue
            )
        )}
    }

    private suspend fun binarySearch() {
        val targetValue = state.value.targetValue ?: return
        val data = state.value.data
        var left = 0
        var right = data.size - 1

        while (left <= right) {
            if (!state.value.isSearching) return

            val mid = (left + right) / 2
            _state.update { it.copy(
                highlightedIndices = setOf(mid),
                comparisonMessage = getApplication<Application>().getString(
                    R.string.comparing_with_target,
                    data[mid],
                    targetValue
                )
            )}
            delay(500)

            when {
                data[mid] == targetValue -> {
                    timerJob?.cancel()
                    _state.update { it.copy(
                        foundIndex = mid,
                        isSearching = false,
                        comparisonMessage = getApplication<Application>().getString(
                            R.string.found_target_at_index,
                            targetValue,
                            mid
                        )
                    )}
                    return
                }
                data[mid] < targetValue -> {
                    left = mid + 1
                    _state.update { it.copy(
                        comparisonMessage = getApplication<Application>().getString(
                            R.string.searching_right_half
                        )
                    )}
                }
                else -> {
                    right = mid - 1
                    _state.update { it.copy(
                        comparisonMessage = getApplication<Application>().getString(
                            R.string.searching_left_half
                        )
                    )}
                }
            }
        }

        timerJob?.cancel()
        _state.update { it.copy(
            isSearching = false,
            comparisonMessage = getApplication<Application>().getString(
                R.string.target_not_found,
                targetValue
            )
        )}
    }

    private suspend fun jumpSearch() {
        val targetValue = state.value.targetValue ?: return
        val data = state.value.data
        val n = data.size
        var step = kotlin.math.sqrt(n.toDouble()).toInt()
        var prev = 0

        while (data[kotlin.math.min(step, n) - 1] < targetValue) {
            if (!state.value.isSearching) return

            prev = step
            step += kotlin.math.sqrt(n.toDouble()).toInt()
            _state.update { it.copy(
                highlightedIndices = setOf(prev),
                comparisonMessage = getApplication<Application>().getString(
                    R.string.jumping_to_block,
                    prev
                )
            )}
            delay(500)

            if (prev >= n) {
                timerJob?.cancel()
                _state.update { it.copy(
                    isSearching = false,
                    comparisonMessage = getApplication<Application>().getString(
                        R.string.target_not_found,
                        targetValue
                    )
                )}
                return
            }
        }

        while (data[prev] < targetValue) {
            if (!state.value.isSearching) return

            prev++
            _state.update { it.copy(
                highlightedIndices = setOf(prev),
                comparisonMessage = getApplication<Application>().getString(
                    R.string.linear_scanning,
                    prev
                )
            )}
            delay(500)

            if (prev == kotlin.math.min(step, n)) {
                timerJob?.cancel()
                _state.update { it.copy(
                    isSearching = false,
                    comparisonMessage = getApplication<Application>().getString(
                        R.string.target_not_found,
                        targetValue
                    )
                )}
                return
            }
        }

        if (data[prev] == targetValue) {
            timerJob?.cancel()
            _state.update { it.copy(
                foundIndex = prev,
                isSearching = false,
                comparisonMessage = getApplication<Application>().getString(
                    R.string.found_target_at_index,
                    targetValue,
                    prev
                )
            )}
            return
        }

        timerJob?.cancel()
        _state.update { it.copy(
            isSearching = false,
            comparisonMessage = getApplication<Application>().getString(
                R.string.target_not_found,
                targetValue
            )
        )}
    }
} 