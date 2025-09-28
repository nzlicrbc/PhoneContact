package com.example.phonecontact.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.phonecontact.ui.theme.Blue
import com.example.phonecontact.ui.theme.Dimensions

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Blue,
            strokeWidth = Dimensions.progressIndicatorStroke
        )
    }
}