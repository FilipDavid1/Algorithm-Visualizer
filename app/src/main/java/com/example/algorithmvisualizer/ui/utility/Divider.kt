package com.example.algorithmvisualizer.ui.utility

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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