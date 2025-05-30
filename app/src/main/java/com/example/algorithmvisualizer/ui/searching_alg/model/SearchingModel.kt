package com.example.algorithmvisualizer.ui.searching_alg.model

data class SearchingState(
    val data: List<Int> = emptyList(),
    val highlightedIndices: Set<Int> = emptySet(),
    val foundIndex: Int? = null,
    val isSearching: Boolean = false,
    val selectedAlgorithm: String = "",
    val targetValue: Int? = null,
    val comparisonMessage: String = "",
    val elapsedTimeMs: Long = 0L
)

sealed class SearchingAlgorithm {
    data object LinearSearch : SearchingAlgorithm()
    data object BinarySearch : SearchingAlgorithm()
    data object JumpSearch : SearchingAlgorithm()

    companion object {
        fun fromString(name: String): SearchingAlgorithm {
            return when (name) {
                "Linear Search" -> LinearSearch
                "Binary Search" -> BinarySearch
                "Jump Search" -> JumpSearch
                else -> LinearSearch
            }
        }
    }
}

sealed class SearchingEvent {
    data class SelectAlgorithm(val algorithm: String) : SearchingEvent()
    data class SetTargetValue(val value: Int) : SearchingEvent()
    data object StartSearching : SearchingEvent()
    data object StopSearching : SearchingEvent()
    data object ResetData : SearchingEvent()
}