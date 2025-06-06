package com.example.algorithmvisualizer.ui.searching_alg

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.algorithmvisualizer.R
import com.example.algorithmvisualizer.ui.searching_alg.model.SearchingEvent
import com.example.algorithmvisualizer.ui.theme.BlueContainer
import com.example.algorithmvisualizer.ui.theme.WhiteText
import com.example.algorithmvisualizer.ui.theme.YellowContainer
import com.example.algorithmvisualizer.ui.theme.yellowDropdownColors
import com.example.algorithmvisualizer.ui.utility.DashedDivider
import com.example.algorithmvisualizer.ui.common.AlgorithmInfoScreen
import androidx.compose.material3.MenuAnchorType
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchingScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchingListViewModel = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val algorithms by viewModel.algorithms.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var showAlgorithmInfo by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    if (showAlgorithmInfo) {
        val timeComplexity = when (state.selectedAlgorithm) {
            stringResource(R.string.linear_search) -> stringResource(R.string.linear_search_time)
            stringResource(R.string.binary_search) -> stringResource(R.string.binary_search_time)
            stringResource(R.string.jump_search) -> stringResource(R.string.jump_search_time)
            else -> ""
        }
        val spaceComplexity = when (state.selectedAlgorithm) {
            stringResource(R.string.linear_search) -> stringResource(R.string.linear_search_space)
            stringResource(R.string.binary_search) -> stringResource(R.string.binary_search_space)
            stringResource(R.string.jump_search) -> stringResource(R.string.jump_search_space)
            else -> ""
        }
        val logic = when (state.selectedAlgorithm) {
            stringResource(R.string.linear_search) -> stringResource(R.string.linear_search_logic)
            stringResource(R.string.binary_search) -> stringResource(R.string.binary_search_logic)
            stringResource(R.string.jump_search) -> stringResource(R.string.jump_search_logic)
            else -> ""
        }
        val useCases = when (state.selectedAlgorithm) {
            stringResource(R.string.linear_search) -> stringResource(R.string.linear_search_uses)
            stringResource(R.string.binary_search) -> stringResource(R.string.binary_search_uses)
            stringResource(R.string.jump_search) -> stringResource(R.string.jump_search_uses)
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
            modifier = modifier
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
                        text = stringResource(R.string.searching_algorithms),
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
                    .fillMaxWidth(),
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(containerColor = BlueContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Algorithm selection dropdown
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        TextField(
                            value = state.selectedAlgorithm,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, enabled = true),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = YellowContainer,
                                unfocusedContainerColor = YellowContainer,
                                focusedTextColor = WhiteText,
                                unfocusedTextColor = WhiteText,
                                cursorColor = WhiteText,
                                focusedIndicatorColor = WhiteText,
                                unfocusedIndicatorColor = WhiteText
                            )
                        )

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
                                            viewModel.onEvent(SearchingEvent.SelectAlgorithm(algorithm))
                                            expanded = false
                                        },
                                        modifier = Modifier.background(YellowContainer)
                                    )
                                    DashedDivider(color = BlueContainer)
                                }
                            }
                        }
                    }

                    DashedDivider()

                    // Array visualization
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        state.data.forEachIndexed { index, value ->
                            ArrayElement(
                                value = value,
                                isHighlighted = index in state.highlightedIndices,
                                isFound = state.foundIndex == index,
                                isSearching = state.isSearching
                            )
                        }
                    }

                    DashedDivider()

                    // Target value input
                    val focusManager = LocalFocusManager.current
                    TextField(
                        value = state.targetValue?.toString() ?: "",
                        onValueChange = { value ->
                            value.toIntOrNull()?.let {
                                viewModel.onEvent(SearchingEvent.SetTargetValue(it))
                            }
                        },
                        label = { Text(stringResource(R.string.insert_search_number), color = WhiteText) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = YellowContainer,
                            unfocusedContainerColor = YellowContainer,
                            focusedTextColor = WhiteText,
                            unfocusedTextColor = WhiteText,
                            cursorColor = WhiteText,
                            focusedIndicatorColor = WhiteText,
                            unfocusedIndicatorColor = WhiteText,
                            focusedLabelColor = WhiteText,
                            unfocusedLabelColor = WhiteText
                        )
                    )

                    DashedDivider()

                    // Status message
                    Text(
                        text = if (state.isSearching) {
                            state.comparisonMessage
                        } else {
                            if (state.comparisonMessage.isEmpty()) {
                                stringResource(R.string.press_search_to_start, state.selectedAlgorithm)
                            } else {
                                state.comparisonMessage
                            }
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = WhiteText,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    )

                    DashedDivider()

                    // Timer
                    Text(
                        text = stringResource(R.string.time_elapsed, state.elapsedTimeMs / 1000f),
                        style = MaterialTheme.typography.bodyMedium,
                        color = WhiteText,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    DashedDivider()

                    // Algorithm Description
                    val algorithmDescription = when (state.selectedAlgorithm) {
                        stringResource(R.string.linear_search) -> stringResource(R.string.linear_search_desc)
                        stringResource(R.string.binary_search) -> stringResource(R.string.binary_search_desc)
                        stringResource(R.string.jump_search) -> stringResource(R.string.jump_search_desc)
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
                            text = stringResource(R.string.algorithm_info),
                            color = WhiteText,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                }
            }

            // Step Mode Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.step_mode),
                    color = BlueContainer,
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = state.isStepMode,
                    onCheckedChange = { viewModel.onEvent(SearchingEvent.ToggleStepMode) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = WhiteText,
                        checkedTrackColor = BlueContainer,
                        uncheckedThumbColor = WhiteText,
                        uncheckedTrackColor = YellowContainer
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                                onClick = { viewModel.onEvent(SearchingEvent.StepBackward) },
                                enabled = state.canStepBackward,
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        color = YellowContainer,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = stringResource(R.string.previous_step),
                                    tint = WhiteText,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .graphicsLayer(
                                            alpha = if (state.canStepBackward) 1f else 0.5f
                                        )
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(
                                        R.string.step_progress,
                                        state.currentStep,
                                        state.totalSteps
                                    ),
                                    color = WhiteText,
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                LinearProgressIndicator(
                                    progress = { state.currentStep.toFloat() / state.totalSteps.toFloat() },
                                    modifier = Modifier
                                        .width(120.dp)
                                        .padding(top = 8.dp),
                                    color = YellowContainer,
                                    trackColor = BlueContainer.copy(alpha = 0.2f),
                                )
                            }

                            IconButton(
                                onClick = { viewModel.onEvent(SearchingEvent.StepForward) },
                                enabled = state.canStepForward,
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        color = YellowContainer,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = stringResource(R.string.next_step),
                                    tint = WhiteText,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .graphicsLayer(
                                            alpha = if (state.canStepForward) 1f else 0.5f
                                        )
                                )
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = { viewModel.onEvent(SearchingEvent.StartSearching) },
                                enabled = !state.isSearching && state.targetValue != null,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = YellowContainer,
                                    contentColor = WhiteText
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(stringResource(R.string.search))
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Button(
                                onClick = { viewModel.onEvent(SearchingEvent.ResetData) },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = YellowContainer,
                                    contentColor = WhiteText
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(stringResource(R.string.reset))
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
                        onClick = { viewModel.onEvent(SearchingEvent.StartSearching) },
                        enabled = !state.isSearching && state.targetValue != null,
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
                        Text(stringResource(R.string.search))
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = { viewModel.onEvent(SearchingEvent.StopSearching) },
                        enabled = state.isSearching,
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
                        Text(stringResource(R.string.stop))
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = { viewModel.onEvent(SearchingEvent.ResetData) },
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
                        Text(stringResource(R.string.reset))
                    }
                }
            }
        }
    }
}

@Composable
fun ArrayElement(
    value: Int,
    isHighlighted: Boolean,
    isFound: Boolean,
    isSearching: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ArrayElementTransition")
    
    val highlightScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ScaleAnimation"
    )

    val borderWidth by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "BorderAnimation"
    )

    Box(
        modifier = modifier
            .size(40.dp)
            .scale(if (isHighlighted && isSearching) highlightScale else 1f)
            .background(
                when {
                    isFound -> MaterialTheme.colorScheme.primary
                    isHighlighted -> YellowContainer
                    else -> MaterialTheme.colorScheme.surfaceVariant
                },
                RoundedCornerShape(4.dp)
            )
            .then(
                if (isHighlighted || isFound) {
                    Modifier.border(
                        width = if (isSearching) borderWidth.dp else 2.dp,
                        color = if (isFound) Color.Green else WhiteText,
                        shape = RoundedCornerShape(4.dp)
                    )
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.bodyLarge,
            color = WhiteText
        )
    }
} 