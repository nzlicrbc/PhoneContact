package com.example.phonecontact.presentation.addcontact

import com.example.phonecontact.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
                .padding(bottom = Dimensions.paddingExtraLarge)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onCameraSelected()
                        onDismiss()
                    }
                    .padding(
                        horizontal = Dimensions.paddingMedium,
                        vertical = Dimensions.paddingMedium
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.camera),
                    contentDescription = stringResource(R.string.camera),
                    modifier = Modifier.size(Dimensions.iconSizeMedium),
                    tint = Blue
                )
                Spacer(modifier = Modifier.width(Dimensions.spacingMedium))
                Text(
                    text = stringResource(R.string.camera),
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary
                )
            }

            Divider(color = DividerColor, thickness = Dimensions.dividerThickness)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onGallerySelected()
                        onDismiss()
                    }
                    .padding(
                        horizontal = Dimensions.paddingMedium,
                        vertical = Dimensions.paddingMedium
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.photo),
                    contentDescription = stringResource(R.string.gallery),
                    modifier = Modifier.size(Dimensions.iconSizeMedium),
                    tint = Blue
                )
                Spacer(modifier = Modifier.width(Dimensions.spacingMedium))
                Text(
                    text = stringResource(R.string.gallery),
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary
                )
            }
        }
    }
}