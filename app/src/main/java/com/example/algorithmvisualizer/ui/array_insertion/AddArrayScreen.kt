package com.example.algorithmvisualizer.ui.array_insertion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.algorithmvisualizer.R
import com.example.algorithmvisualizer.ui.theme.*
import kotlin.random.Random

@Composable
fun AddArrayScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: ArraysViewModel = viewModel()
) {
    var arraySize by rememberSaveable { mutableStateOf("10") }
    var numbers by rememberSaveable { mutableStateOf(List(10) { Random.nextInt(1, 100) }) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    val errorColor = Color(0xFFFF6B6B)
    val invalidInputMessage = stringResource(R.string.invalid_input)
    val arraySizeErrorMessage = stringResource(R.string.array_size_error)
    val maxArraySize = 100

    fun validateAndUpdateSize(newSize: String) {
        arraySize = newSize
        val size = newSize.toIntOrNull()
        when {
            size == null -> {
                showError = true
                errorMessage = invalidInputMessage
            }
            size <= 0 -> {
                showError = true
                errorMessage = arraySizeErrorMessage
            }
            size > maxArraySize -> {
                showError = true
                errorMessage = "Maximum array size is $maxArraySize"
            }
            else -> {
                numbers = List(size) { Random.nextInt(1, 100) }
                showError = false
                errorMessage = ""
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = YellowContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = WhiteText
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.navigate_back)
                    )
                }
                Text(
                    text = stringResource(R.string.add_new_array),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = WhiteText
                )
                Spacer(modifier = Modifier.width(48.dp))
            }
        }

        // Array size input card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = BlueContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.array_size),
                    style = MaterialTheme.typography.titleMedium,
                    color = WhiteText,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                val focusManager = LocalFocusManager.current
                OutlinedTextField(
                    value = arraySize,
                    onValueChange = { validateAndUpdateSize(it) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = YellowContainer,
                        unfocusedBorderColor = WhiteText,
                        focusedTextColor = WhiteText,
                        unfocusedTextColor = WhiteText,
                        errorBorderColor = errorColor,
                        errorTextColor = errorColor
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    isError = showError
                )
                if (showError) {
                    Text(
                        text = errorMessage,
                        color = errorColor,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Controls card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = BlueContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        val size = arraySize.toIntOrNull() ?: 10
                        if (size > 0) {
                            numbers = List(size) { Random.nextInt(1, 100) }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = YellowContainer
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = stringResource(R.string.generate_new_numbers))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.generate_new))
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        val size = arraySize.toIntOrNull()
                        if (size != null && size > 0 && size <= maxArraySize) {
                            viewModel.addArray(numbers)
                            onNavigateBack()
                        } else {
                            showError = true
                            errorMessage = invalidInputMessage
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = YellowContainer
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(R.string.save_array))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.save_array))
                }
            }
        }

        // Array preview card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = BlueContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.array_preview),
                    style = MaterialTheme.typography.titleMedium,
                    color = WhiteText,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(numbers) { number ->
                        Card(
                            modifier = Modifier
                                .size(48.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = YellowContainer
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = number.toString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = WhiteText,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
} 