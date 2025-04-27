package com.example.algorithmvisualizer.ui.theme

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun yellowDropdownColors(): TextFieldColors = TextFieldDefaults.colors(
    focusedContainerColor = YellowContainer,
    unfocusedContainerColor = YellowContainer,
    focusedIndicatorColor = YellowContainer,
    unfocusedIndicatorColor = YellowContainer,
    focusedTextColor = WhiteText,        // <<< farba textu
    unfocusedTextColor = WhiteText,      // <<< farba textu keď nie je focus
    focusedTrailingIconColor = WhiteText, // <<< farba ikonky keď je focus
    unfocusedTrailingIconColor = WhiteText, // <<< farba ikonky keď nie je focus
)
