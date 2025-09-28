package com.example.phonecontact.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.phonecontact.domain.model.Contact
import com.example.phonecontact.ui.theme.*

@Composable
fun ContactListItem(
    contact: Contact,
    onClick: () -> Unit,
    showDivider: Boolean = true,
    isInDeviceContacts: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceWhite)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = Dimensions.paddingMedium)
                .height(Dimensions.contactItemHeight),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ContactAvatar(
                imageUrl = contact.profileImageUrl,
                name = contact.firstName,
                size = Dimensions.profileImageSize,
                enableColorShadow = true
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${contact.firstName} ${contact.lastName}".trim(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary
                )

                contact.phoneNumber?.let { phone ->
                    Text(
                        text = phone,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            if (isInDeviceContacts) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "In device contacts",
                    modifier = Modifier.size(Dimensions.iconSizeSmall),
                    tint = Blue
                )
            }
        }

        if (showDivider) {
            DividerLine(
                startIndent = Dimensions.paddingMedium + Dimensions.profileImageSize + 12.dp
            )
        }
    }
}