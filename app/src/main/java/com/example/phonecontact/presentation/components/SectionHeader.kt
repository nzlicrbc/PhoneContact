package com.example.phonecontact.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.phonecontact.ui.theme.*

@Composable
fun SectionHeader(
    letter: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimensions.paddingMedium, vertical = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = SurfaceWhite
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimensions.paddingSmall),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = letter.uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }

        DividerLine(
            modifier = Modifier.fillMaxWidth()
        )
    }
}
