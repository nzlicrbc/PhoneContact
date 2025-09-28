package com.example.phonecontact.presentation.components

import com.example.phonecontact.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.phonecontact.domain.model.Contact
import com.example.phonecontact.ui.theme.*

@Composable
fun ContactListItem(
    contact: Contact,
    onClick: () -> Unit,
    showDivider: Boolean = true,
    modifier: Modifier = Modifier
) {
    val isInDeviceContacts = contact.isInDeviceContacts ?: false

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
            Box(
                modifier = Modifier.size(Dimensions.profileImageSize)
            ) {
                ContactAvatar(
                    imageUrl = contact.profileImageUrl,
                    name = contact.firstName,
                    size = Dimensions.profileImageSize,
                    enableColorShadow = false
                )

                if (isInDeviceContacts) {
                    Box(
                        modifier = Modifier
                            .size(Dimensions.iconSizeMedium)
                            .background(
                                color = Blue,
                                shape = CircleShape
                            )
                            .align(Alignment.BottomEnd)
                            .offset(x = Dimensions.profileIconOffsetX, y = Dimensions.profileIconOffsetY)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = stringResource(R.string.in_device_contacts),
                            modifier = Modifier.size(Dimensions.iconSizeSmall),
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(Dimensions.paddingMedium))

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
        }

        if (showDivider) {
            DividerLine(
                startIndent = Dimensions.paddingMedium + Dimensions.profileImageSize + Dimensions.paddingSmall
            )
        }
    }
}