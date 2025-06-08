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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import com.example.algorithmvisualizer.ui.common.AlgorithmInfoScreen
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
    var scrollState = rememberScrollState()

    if (showAlgorithmInfo) {
        val timeComplexity = when (state.selectedAlgorithm) {
            stringResource(R.string.bubble_sort) -> stringResource(R.string.bubble_sort_time)
            stringResource(R.string.selection_sort) -> stringResource(R.string.selection_sort_time)
            stringResource(R.string.insertion_sort) -> stringResource(R.string.insertion_sort_time)
            else -> ""
        }
        val spaceComplexity = when (state.selectedAlgorithm) {
            stringResource(R.string.bubble_sort) -> stringResource(R.string.bubble_sort_space)
            stringResource(R.string.selection_sort) -> stringResource(R.string.selection_sort_space)
            stringResource(R.string.insertion_sort) -> stringResource(R.string.insertion_sort_space)
            else -> ""
        }
        val logic = when (state.selectedAlgorithm) {
            stringResource(R.string.bubble_sort) -> stringResource(R.string.bubble_sort_logic)
            stringResource(R.string.selection_sort) -> stringResource(R.string.selection_sort_logic)
            stringResource(R.string.insertion_sort) -> stringResource(R.string.insertion_sort_logic)
            else -> ""
        }
        val useCases = when (state.selectedAlgorithm) {
            stringResource(R.string.bubble_sort) -> stringResource(R.string.bubble_sort_uses)
            stringResource(R.string.selection_sort) -> stringResource(R.string.selection_sort_uses)
            stringResource(R.string.insertion_sort) -> stringResource(R.string.insertion_sort_uses)
            else -> ""
        }
        AlgorithmInfoScreen(
            algorithm = state.selectedAlgorithm,
            timeComplexity = timeComplexity,
            spaceComplexity = spaceComplexity,
            algorithmLogic = logic,
            useCases = useCases,
            onDismiss = { showAlgorithmInfo = false }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                        text = stringResource(R.string.pick_algorithm),
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
                                    .fillMaxWidth()
                                    .height(150.dp)
                                    .padding(16.dp)
                            )

                            Text(
                                text = if (state.isSorting) {
                                    state.comparisonMessage
                                } else {
                                    if (state.comparisonMessage.isEmpty()) {
                                        stringResource(R.string.press_sort_to_start, state.selectedAlgorithm)
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
                                    stringResource(R.string.timer_display, state.elapsedTimeMs / 1000.0f)
                                } else {
                                    stringResource(R.string.timer_zero)
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
                                stringResource(R.string.bubble_sort) -> stringResource(R.string.bubble_sort_desc)
                                stringResource(R.string.selection_sort) -> stringResource(R.string.selection_sort_desc)
                                stringResource(R.string.insertion_sort) -> stringResource(R.string.insertion_sort_desc)
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
                                    text = stringResource(R.string.info_button_hint),
                                    color = WhiteText,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Step Mode Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.step_mode),
                    color = BlueContainer,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Switch(
                    checked = state.isStepMode,
                    onCheckedChange = { viewModel.onEvent(SortingEvent.ToggleStepMode) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = WhiteText,
                        checkedTrackColor = BlueContainer,
                        uncheckedThumbColor = WhiteText,
                        uncheckedTrackColor = YellowContainer
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (state.isStepMode) {
                // Step-by-Step Controls
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (state.totalSteps > 0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { viewModel.onEvent(SortingEvent.StepBackward) },
                                enabled = state.canStepBackward,
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        color = if (state.canStepBackward) YellowContainer else YellowContainer.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = stringResource(R.string.previous_step),
                                    tint = WhiteText,
                                    modifier = Modifier
                                        .size(32.dp)
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                Text(
                                    text = stringResource(
                                        R.string.step_progress,
                                        state.currentStep,
                                        state.totalSteps
                                    ),
                                    color = WhiteText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                LinearProgressIndicator(
                                    progress = { state.currentStep.toFloat() / state.totalSteps.toFloat() },
                                    modifier = Modifier
                                        .width(160.dp)
                                        .height(6.dp),
                                    color = YellowContainer,
                                    trackColor = BlueContainer.copy(alpha = 0.2f)
                                )
                            }

                            IconButton(
                                onClick = { viewModel.onEvent(SortingEvent.StepForward) },
                                enabled = state.canStepForward,
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        color = if (state.canStepForward) YellowContainer else YellowContainer.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = stringResource(R.string.next_step),
                                    tint = WhiteText,
                                    modifier = Modifier
                                        .size(32.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = { viewModel.onEvent(SortingEvent.StartSorting) },
                                enabled = !state.isSorting,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = YellowContainer,
                                    contentColor = WhiteText,
                                    disabledContainerColor = YellowContainer.copy(alpha = 0.5f),
                                    disabledContentColor = WhiteText.copy(alpha = 0.5f)
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = stringResource(R.string.sort_button),
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Button(
                                onClick = { viewModel.onEvent(SortingEvent.ResetData) },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = YellowContainer,
                                    contentColor = WhiteText
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = stringResource(R.string.reset_button),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            } else {
                // Default Controls
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
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
                        Text(stringResource(R.string.sort_button))
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
                        Text(stringResource(R.string.stop_button))
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = { viewModel.onEvent(SortingEvent.ResetData) },
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
                        Text(stringResource(R.string.reset_button))
                    }
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

@Preview(showBackground = true)
@Composable
fun PreviewSortingListScreen() {
    MaterialTheme {
        SortingListScreen()
    }
}