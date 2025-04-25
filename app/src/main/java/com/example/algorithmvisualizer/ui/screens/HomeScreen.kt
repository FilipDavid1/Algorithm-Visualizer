package com.example.algorithmvisualizer.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.algorithmvisualizer.ui.theme.WhiteText
import com.example.algorithmvisualizer.ui.theme.YellowContainer

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
            Text(
                text = "Algorithm Visualizer",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = WhiteText,
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 24.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DashedDivider(
    color: Color = Color.LightGray,
    strokeWidth: Float = 2f,
    dashLength: Float = 10f,
    gapLength: Float = 20f
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(strokeWidth.dp)
    ) {
        val totalWidth = size.width
        var startX = 0f
        while (startX < totalWidth) {
            val endX = (startX + dashLength).coerceAtMost(totalWidth)
            drawLine(
                color = color,
                start = androidx.compose.ui.geometry.Offset(x = startX, y = 0f),
                end = androidx.compose.ui.geometry.Offset(x = endX, y = 0f),
                strokeWidth = strokeWidth
            )
            startX += dashLength + gapLength
        }
    }
}
