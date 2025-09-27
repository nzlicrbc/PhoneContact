package com.example.phonecontact.presentation.addcontact

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.phonecontact.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoSelectionBottomSheet(
    onDismiss: () -> Unit,
    onCameraSelected: () -> Unit,
    onGallerySelected: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = SurfaceWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onCameraSelected()
                        onDismiss()
                    }
                    .padding(horizontal = Dimensions.paddingMedium, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Camera,
                    contentDescription = "Camera",
                    modifier = Modifier.size(Dimensions.iconSizeMedium),
                    tint = Blue
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Camera",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary
                )
            }

            Divider(color = DividerColor, thickness = 0.5.dp)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onGallerySelected()
                        onDismiss()
                    }
                    .padding(horizontal = Dimensions.paddingMedium, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Collections,
                    contentDescription = "Gallery",
                    modifier = Modifier.size(Dimensions.iconSizeMedium),
                    tint = Blue
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Gallery",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary
                )
            }
        }
    }
}