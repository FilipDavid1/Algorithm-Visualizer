package com.example.algorithmvisualizer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorithmvisualizer.data.ArrayData
import com.example.algorithmvisualizer.data.ArrayRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

data class ArraysState(
    val arrays: List<ArrayData> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ArraysViewModel : ViewModel() {
    private val repository = ArrayRepository()
    private val _state = MutableStateFlow(ArraysState())


    fun addArray(numbers: List<Int>) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, error = null)
                repository.addArray(numbers)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to add array"
                )
            }
        }
    }
} 