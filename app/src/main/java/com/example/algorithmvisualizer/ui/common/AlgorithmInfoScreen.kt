package com.example.algorithmvisualizer.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.algorithmvisualizer.R
import com.example.algorithmvisualizer.ui.theme.BlueContainer
import com.example.algorithmvisualizer.ui.theme.WhiteText
import com.example.algorithmvisualizer.ui.theme.YellowContainer
import com.example.algorithmvisualizer.ui.utility.DashedDivider

@Composable
fun AlgorithmInfoScreen(
    algorithm: String,
    timeComplexity: String,
    spaceComplexity: String,
    algorithmLogic: String,
    useCases: String,
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
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                    text = stringResource(R.string.time_complexity),
                    style = MaterialTheme.typography.titleLarge,
                    color = WhiteText,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
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
                    text = stringResource(R.string.space_complexity),
                    style = MaterialTheme.typography.titleLarge,
                    color = WhiteText,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
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
                    text = stringResource(R.string.algorithm_logic),
                    style = MaterialTheme.typography.titleLarge,
                    color = WhiteText,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = algorithmLogic,
                    color = WhiteText,
                    modifier = Modifier.padding(start = 16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))
                DashedDivider(color = BlueContainer)
                Spacer(modifier = Modifier.height(24.dp))

                // Best Use Cases Section
                Text(
                    text = stringResource(R.string.best_use_cases),
                    style = MaterialTheme.typography.titleLarge,
                    color = WhiteText,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = useCases,
                    color = WhiteText,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
} 