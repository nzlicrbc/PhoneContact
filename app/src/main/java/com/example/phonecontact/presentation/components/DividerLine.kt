package com.example.phonecontact.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.phonecontact.ui.theme.DividerColor

@Composable
fun DividerLine(
    modifier: Modifier = Modifier,
    startIndent: Dp = 0.dp
) {
    Divider(
        modifier = modifier.padding(start = startIndent),
        color = DividerColor,
        thickness = 0.5.dp
    )
}