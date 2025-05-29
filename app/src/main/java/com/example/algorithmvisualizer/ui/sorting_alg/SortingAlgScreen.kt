package com.example.algorithmvisualizer.ui.sorting_alg

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.algorithmvisualizer.R
import com.example.algorithmvisualizer.ui.theme.BlueContainer
import com.example.algorithmvisualizer.ui.theme.WhiteText
import com.example.algorithmvisualizer.ui.theme.YellowContainer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MenuAnchorType
import com.example.algorithmvisualizer.ui.theme.yellowDropdownColors
import com.example.algorithmvisualizer.ui.utility.DashedDivider
import com.example.algorithmvisualizer.ui.sorting_alg.model.SortingEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortingListScreen(
    viewModel: SortingListViewModel = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val algorithms by viewModel.algorithms.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var showAlgorithmInfo by remember { mutableStateOf(false) }

    if (showAlgorithmInfo) {
        AlgorithmInfoScreen(
            algorithm = state.selectedAlgorithm,
            onDismiss = { showAlgorithmInfo = false }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = YellowContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = WhiteText
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                    Text(
                        text = stringResource(R.string.sorting_algorithms_title),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = WhiteText,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 48.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(containerColor = BlueContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    // Title
                    Text(
                        text = "Pick sorting algorithm:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = WhiteText,
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Dropdown menu for algorithm selection
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        TextField(
                            value = state.selectedAlgorithm,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            colors = yellowDropdownColors(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryEditable, enabled = true)
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(YellowContainer)
                        ) {
                            algorithms.forEach { algorithm ->
                                DropdownMenuItem(
                                    text = { Text(text = algorithm, color = WhiteText) },
                                    onClick = {
                                        viewModel.onEvent(SortingEvent.SelectAlgorithm(algorithm))
                                        expanded = false
                                    },
                                    modifier = Modifier.background(YellowContainer)
                                )
                                DashedDivider(color = BlueContainer)
                            }
                        }
                    }

                    // Column Chart Visualization
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(vertical = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = BlueContainer)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            ColumnChart(
                                data = state.data,
                                highlightedIndices = state.highlightedIndices,
                                sortedIndices = state.sortedIndices,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )

                            Text(
                                text = if (state.isSorting) {
                                    state.comparisonMessage
                                } else {
                                    if (state.comparisonMessage.isEmpty()) {
                                        "Press sort to start ${state.selectedAlgorithm} algorithm"
                                    } else {
                                        state.comparisonMessage
                                    }
                                },
                                color = WhiteText,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                            DashedDivider(color = YellowContainer)
                            Spacer(modifier = Modifier.height(10.dp))

                            // Timer display
                            Text(
                                text = if (state.elapsedTimeMs > 0) {
                                    "Time: ${String.format("%.2f", state.elapsedTimeMs / 1000.0)} seconds"
                                } else {
                                    "Time: 0.00 seconds"
                                },
                                color = WhiteText,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                            DashedDivider(color = YellowContainer)
                            Spacer(modifier = Modifier.height(10.dp))

                            // Algorithm Description
                            val algorithmDescription = when (state.selectedAlgorithm) {
                                "Bubble Sort" -> "Bubble Sort repeatedly steps through the list, compares adjacent elements and swaps them if they are in the wrong order. The pass through the list is repeated until no swaps are needed."
                                "Selection Sort" -> "Selection Sort divides the array into a sorted and unsorted region, repeatedly finding the minimum element from the unsorted region and placing it at the end of the sorted region."
                                "Insertion Sort" -> "Insertion Sort builds the final sorted array one item at a time, by repeatedly inserting a new element into the sorted portion of the array."
                                "Merge Sort" -> "Merge Sort is a divide-and-conquer algorithm that recursively breaks down the array into smaller subarrays, sorts them, and then merges them back together in sorted order."
                                else -> ""
                            }

                            Text(
                                text = algorithmDescription,
                                color = WhiteText,
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                fontSize = 14.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                            DashedDivider(color = YellowContainer)
                            Spacer(modifier = Modifier.height(10.dp))

                            // Info Button Row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = { showAlgorithmInfo = true },
                                    shape = RoundedCornerShape(5.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = WhiteText
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Algorithm Info",
                                        tint = BlueContainer,
                                        modifier = Modifier.size(25.dp)
                                    )
                                }
                                Text(
                                    text = "Click to dive into logic, code & O(n)",
                                    color = WhiteText,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { viewModel.onEvent(SortingEvent.StartSorting) },
                    enabled = !state.isSorting,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = YellowContainer,
                        contentColor = WhiteText
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 4.dp
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("SORT")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = { viewModel.onEvent(SortingEvent.StopSorting) },
                    enabled = state.isSorting,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = YellowContainer,
                        contentColor = WhiteText
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 4.dp
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("STOP")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = { viewModel.onEvent(SortingEvent.ResetData) },
                    enabled = !state.isSorting,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = YellowContainer,
                        contentColor = WhiteText
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 4.dp
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("RESET")
                }
            }
        }
    }
}

@Composable
fun ColumnChart(
    data: List<Int>,
    highlightedIndices: Set<Int>,
    sortedIndices: Set<Int>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val barWidth = size.width / data.size
        val maxValue = data.maxOrNull()?.toFloat() ?: 1f
        val scaleFactor = size.height / maxValue

        data.forEachIndexed { index, value ->
            val isHighlighted = index in highlightedIndices
            val isSorted = index in sortedIndices
            val color = when {
                isHighlighted -> YellowContainer
                isSorted -> Color.Green
                else -> Color.White
            }
            val height = value * scaleFactor

            drawRect(
                color = color,
                topLeft = Offset(
                    x = index * barWidth + (barWidth * 0.1f),
                    y = size.height - height
                ),
                size = Size(
                    width = barWidth * 0.8f,
                    height = height
                ),
                alpha = if (isHighlighted) 1f else 0.8f
            )
        }
    }
}

@Composable
fun AlgorithmInfoScreen(
    algorithm: String,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueContainer)
            .padding(16.dp)
    ) {
        // Header with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onDismiss,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = YellowContainer,
                    contentColor = WhiteText
                )
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Text(
                text = algorithm,
                style = MaterialTheme.typography.headlineMedium,
                color = WhiteText,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(48.dp)) // For balance
        }

        // Content
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            colors = CardDefaults.cardColors(containerColor = YellowContainer)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Time Complexity Section
                Text(
                    text = "Time Complexity",
                    style = MaterialTheme.typography.titleLarge,
                    color = WhiteText,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                val timeComplexity = when (algorithm) {
                    "Bubble Sort" -> "Best: O(n)\nAverage: O(n²)\nWorst: O(n²)"
                    "Selection Sort" -> "Best: O(n²)\nAverage: O(n²)\nWorst: O(n²)"
                    "Insertion Sort" -> "Best: O(n)\nAverage: O(n²)\nWorst: O(n²)"
                    "Merge Sort" -> "Best: O(n log n)\nAverage: O(n log n)\nWorst: O(n log n)"
                    else -> ""
                }
                Text(
                    text = timeComplexity,
                    color = WhiteText,
                    modifier = Modifier.padding(start = 16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))
                DashedDivider(color = BlueContainer)
                Spacer(modifier = Modifier.height(24.dp))

                // Space Complexity Section
                Text(
                    text = "Space Complexity",
                    style = MaterialTheme.typography.titleLarge,
                    color = WhiteText,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                val spaceComplexity = when (algorithm) {
                    "Bubble Sort" -> "O(1)"
                    "Selection Sort" -> "O(1)"
                    "Insertion Sort" -> "O(1)"
                    "Merge Sort" -> "O(n)"
                    else -> ""
                }
                Text(
                    text = spaceComplexity,
                    color = WhiteText,
                    modifier = Modifier.padding(start = 16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))
                DashedDivider(color = BlueContainer)
                Spacer(modifier = Modifier.height(24.dp))

                // Algorithm Logic Section
                Text(
                    text = "Algorithm Logic",
                    style = MaterialTheme.typography.titleLarge,
                    color = WhiteText,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                val logic = when (algorithm) {
                    "Bubble Sort" -> "1. Start with the first element\n2. Compare with next element\n3. Swap if needed\n4. Move to next pair\n5. Repeat until no swaps needed"
                    "Selection Sort" -> "1. Find minimum in unsorted region\n2. Swap with first unsorted element\n3. Expand sorted region\n4. Repeat until array is sorted"
                    "Insertion Sort" -> "1. Start with second element\n2. Compare with previous elements\n3. Shift larger elements right\n4. Insert in correct position\n5. Repeat for all elements"
                    "Merge Sort" -> "1. Divide array in half\n2. Recursively sort both halves\n3. Merge sorted halves\n4. Compare elements\n5. Build final sorted array"
                    else -> ""
                }
                Text(
                    text = logic,
                    color = WhiteText,
                    modifier = Modifier.padding(start = 16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))
                DashedDivider(color = BlueContainer)
                Spacer(modifier = Modifier.height(24.dp))

                // Use Cases Section
                Text(
                    text = "Best Use Cases",
                    style = MaterialTheme.typography.titleLarge,
                    color = WhiteText,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                val useCases = when (algorithm) {
                    "Bubble Sort" -> "• Small datasets\n• Nearly sorted arrays\n• Educational purposes"
                    "Selection Sort" -> "• Small datasets\n• Memory constrained systems\n• When writes are expensive"
                    "Insertion Sort" -> "• Small datasets\n• Nearly sorted arrays\n• Online sorting (stream of data)"
                    "Merge Sort" -> "• Large datasets\n• External sorting\n• Stable sorting required"
                    else -> ""
                }
                Text(
                    text = useCases,
                    color = WhiteText,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSortingListScreen() {
    MaterialTheme {
        SortingListScreen()
    }
}