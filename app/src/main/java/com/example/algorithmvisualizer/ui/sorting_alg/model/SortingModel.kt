package com.example.algorithmvisualizer.ui.sorting_alg.model

data class SortingState(
    val data: List<Int>,
    val highlightedIndices: Set<Int> = emptySet(),
    val sortedIndices: Set<Int> = emptySet(),
    val isSorting: Boolean = false,
    val selectedAlgorithm: String = "Bubble Sort",
    val animationSpeed: Float = 1f,
    val comparisonMessage: String = "",
    val elapsedTimeMs: Long = 0
)

sealed class SortingAlgorithm {
    object BubbleSort : SortingAlgorithm()
    object SelectionSort : SortingAlgorithm()
    object InsertionSort : SortingAlgorithm()
    object MergeSort : SortingAlgorithm()

    companion object {
        fun fromString(name: String): SortingAlgorithm {
            return when (name) {
                "Bubble Sort" -> BubbleSort
                "Selection Sort" -> SelectionSort
                "Insertion Sort" -> InsertionSort
                "Merge Sort" -> MergeSort
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
}