package com.example.algorithmvisualizer.ui.searching_alg

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.algorithmvisualizer.R
import com.example.algorithmvisualizer.ui.searching_alg.model.SearchingEvent
import com.example.algorithmvisualizer.ui.sorting_alg.model.SortingEvent
import com.example.algorithmvisualizer.ui.theme.BlueContainer
import com.example.algorithmvisualizer.ui.theme.WhiteText
import com.example.algorithmvisualizer.ui.theme.YellowContainer
import com.example.algorithmvisualizer.ui.theme.yellowDropdownColors
import com.example.algorithmvisualizer.ui.utility.DashedDivider

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

    Column(
        modifier = modifier.fillMaxSize()
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
                .fillMaxWidth()
                .weight(1f),
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
                        modifier = Modifier.menuAnchor(),
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
                    text = state.comparisonMessage,
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

                DashedDivider()

                // Control buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
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