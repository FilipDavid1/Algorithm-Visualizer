package com.example.algorithmvisualizer.ui.sorting_alg

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
import com.example.algorithmvisualizer.ui.theme.yellowDropdownColors
import com.example.algorithmvisualizer.ui.utility.DashedDivider


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortingListScreen() {
    val viewModel: SortingListViewModel = viewModel()
    val algorithms by viewModel.algorithms.collectAsState()
    var selectedAlgorithm by remember { mutableStateOf("Bubble Sort") }
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
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(containerColor = BlueContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            // Title
            Text(
                text = "Pick sorting algorithm:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = WhiteText,
                modifier = Modifier.padding(16.dp)
            )



            Spacer(modifier = Modifier.height(24.dp))

            /// Dropdown menu for algorithm selection
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.padding(16.dp)
            ) {
                // Text field that shows the selected algorithm
                TextField(
                    value = selectedAlgorithm,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = yellowDropdownColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                )

                // Dropdown menu options
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(YellowContainer)
                ) {
                    algorithms.forEach { algorithm ->
                        DropdownMenuItem(
                            text = { Text(text = algorithm, color = WhiteText) },
                            onClick = {
                                selectedAlgorithm = algorithm
                                expanded = false
                            },
                            modifier = Modifier.background(YellowContainer)
                        )
                        DashedDivider(color = BlueContainer)
                    }
                }
            }
        }

        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { /* Handle sort */ },
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
                onClick = { /* Handle stop */ },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = YellowContainer,
                    contentColor = WhiteText
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 4.dp
                ),
                enabled = false,
                modifier = Modifier.weight(1f)
            ) {
                Text("STOP")
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