package com.example.phonecontact.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.phonecontact.ui.theme.*

@Composable
fun SectionHeader(
    letter: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Background)
            .padding(
                horizontal = Dimensions.paddingMedium,
                vertical = Dimensions.paddingSmall
            )
    ) {
        Text(
            text = letter.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = TextSecondary
        )
    }
}