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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.example.algorithmvisualizer.ui.theme.yellowDropdownColors
import com.example.algorithmvisualizer.ui.utility.DashedDivider
import com.example.algorithmvisualizer.ui.sorting_alg.model.SortingEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortingListScreen(
    viewModel: SortingListViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val algorithms by viewModel.algorithms.collectAsState()
    var expanded by remember { mutableStateOf(false) }

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
            Text(
                text = stringResource(R.string.sorting_algorithms_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = WhiteText,
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 24.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
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

                        DashedDivider(color = YellowContainer)

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

                        DashedDivider(color = YellowContainer)

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
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            fontSize = 14.sp
                        )
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

@Preview(showBackground = true)
@Composable
fun PreviewSortingListScreen() {
    MaterialTheme {
        SortingListScreen()
    }
}