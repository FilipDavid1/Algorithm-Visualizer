package com.example.algorithmvisualizer.ui.sorting_alg.model

data class SortingState(
    val data: List<Int>,
    val highlightedIndices: Set<Int> = emptySet(),
    val sortedIndices: Set<Int> = emptySet(),
    val isSorting: Boolean = false,
    val selectedAlgorithm: String = "Bubble Sort",
    val sortingSpeed: Int = 1,
    val comparisonMessage: String = "",
    val elapsedTimeMs: Long = 0,
    val isStepMode: Boolean = false,
    val currentStep: Int = 0,
    val totalSteps: Int = 0,
    val canStepForward: Boolean = false,
    val canStepBackward: Boolean = false,
    val stepHistory: List<StepState> = emptyList()
)

data class StepState(
    val data: List<Int>,
    val highlightedIndices: Set<Int>,
    val sortedIndices: Set<Int>,
    val comparisonMessage: String
)

sealed class SortingAlgorithm {
    object BubbleSort : SortingAlgorithm()
    object SelectionSort : SortingAlgorithm()
    object InsertionSort : SortingAlgorithm()

    companion object {
        fun fromString(name: String): SortingAlgorithm {
            return when (name) {
                "Bubble Sort" -> BubbleSort
                "Selection Sort" -> SelectionSort
                "Insertion Sort" -> InsertionSort
                else -> BubbleSort
            }
        }
    }
}

sealed class SortingEvent {
    data class SelectAlgorithm(val algorithm: String) : SortingEvent()
    object StartSorting : SortingEvent()
    object StopSorting : SortingEvent()
    object ResetData : SortingEvent()
    object ToggleStepMode : SortingEvent()
    object StepForward : SortingEvent()
    object StepBackward : SortingEvent()
    object LoadNewArray : SortingEvent()
    data class SetSpeed(val speed: Int) : SortingEvent()
}