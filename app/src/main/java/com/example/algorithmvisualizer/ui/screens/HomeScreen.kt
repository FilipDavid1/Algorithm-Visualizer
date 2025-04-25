package com.example.algorithmvisualizer.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.algorithmvisualizer.ui.theme.BlueContainer
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
fun AlgorithmCategoryCard(
    title: String,
    description: String,
    count: Int,
    icon: ImageVector,
    iconOnRight: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = BlueContainer,
            contentColor = WhiteText
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Card Header: Icon and Title
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(!iconOnRight) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        modifier = Modifier
                            .size(85.dp)
                            .background(YellowContainer, CircleShape)
                            .padding(12.dp),
                        tint = WhiteText
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = WhiteText
                    )
                } else {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = WhiteText
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        modifier = Modifier
                            .size(85.dp)
                            .background(YellowContainer, CircleShape)
                            .padding(12.dp),
                        tint = WhiteText
                    )
                }
            }

            DashedDivider()

            // Card Description
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = WhiteText,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            DashedDivider()

            // Card Footer: Count and Button
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    text = "Number of available algorithms:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = WhiteText
                )
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(YellowContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$count",
                        color = WhiteText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }

            DashedDivider()

            Spacer(modifier = Modifier.height(20.dp))

            // Show Button centered
            Box(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Button(
                    onClick = onClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = YellowContainer,
                        contentColor = WhiteText
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 4.dp
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "SHOW",
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
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
