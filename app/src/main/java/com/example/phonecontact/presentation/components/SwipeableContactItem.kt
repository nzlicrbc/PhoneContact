package com.example.phonecontact.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.phonecontact.R
import com.example.phonecontact.domain.model.Contact
import com.example.phonecontact.ui.theme.*
import com.example.phonecontact.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableContactItem(
    contact: Contact,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    showDivider: Boolean = true
) {
    var showDeleteBottomSheet by remember { mutableStateOf(false) }
    var shouldResetSwipe by remember { mutableStateOf(false) }

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            when (dismissValue) {
                SwipeToDismissBoxValue.EndToStart -> {
                    true
                }
                SwipeToDismissBoxValue.Settled -> {
                    true
                }
                else -> false
            }
        }
    )

    LaunchedEffect(showDeleteBottomSheet, shouldResetSwipe) {
        if ((!showDeleteBottomSheet || shouldResetSwipe) &&
            dismissState.currentValue != SwipeToDismissBoxValue.Settled) {
            dismissState.reset()
            shouldResetSwipe = false
        }
    }

    Box {
        SwipeToDismissBox(
            state = dismissState,
            backgroundContent = {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .align(Alignment.CenterEnd),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(Dimensions.swipeActionWidth)
                                .fillMaxHeight()
                                .background(EditBlue)
                                .clickable {
                                    onEdit()
                                    shouldResetSwipe = true
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = stringResource(R.string.edit_contact),
                                tint = Color.White
                            )
                        }

                        Box(
                            modifier = Modifier
                                .width(Dimensions.swipeActionWidth)
                                .fillMaxHeight()
                                .background(DeleteRed)
                                .clickable {
                                    showDeleteBottomSheet = true
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.delete_contact),
                                tint = Color.White
                            )
                        }
                    }
                }
            },
            enableDismissFromStartToEnd = false,
            enableDismissFromEndToStart = true
        ) {
            ContactListItem(
                contact = contact,
                onClick = onClick,
                showDivider = showDivider
            )
        }

        if (showDeleteBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showDeleteBottomSheet = false },
                containerColor = SurfaceWhite,
                dragHandle = {
                    Box(
                        modifier = Modifier
                            .width(Dimensions.bottomSheetDragHandleWidth)
                            .height(Dimensions.bottomSheetDragHandleHeight)
                            .background(
                                TextSecondary.copy(alpha = Constants.ALPHA_LIGHT),
                                RoundedCornerShape(Dimensions.cornerRadiusXSmall)
                            )
                    )
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.paddingXLarge),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.delete_contact_title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = Dimensions.spacingSmall)
                    )

                    Text(
                        text = stringResource(
                            R.string.delete_contact_message,
                            contact.firstName,
                            contact.lastName
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = Dimensions.spacingXLarge)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingLarge)
                    ) {
                        OutlinedButton(
                            onClick = { showDeleteBottomSheet = false },
                            modifier = Modifier
                                .weight(1f)
                                .height(Dimensions.buttonHeightMedium),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = SurfaceWhite,
                                contentColor = TextPrimary
                            ),
                            border = BorderStroke(
                                Dimensions.borderWidthSmall,
                                TextSecondary.copy(alpha = Constants.ALPHA_LIGHT)
                            ),
                            shape = RoundedCornerShape(Dimensions.cornerRadiusLarge)
                        ) {
                            Text(stringResource(R.string.cancel))
                        }

                        Button(
                            onClick = {
                                onDelete()
                                showDeleteBottomSheet = false
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(Dimensions.buttonHeightMedium),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TextPrimary,
                                contentColor = SurfaceWhite
                            ),
                            shape = RoundedCornerShape(Dimensions.cornerRadiusLarge)
                        ) {
                            Text(stringResource(R.string.confirm))
                        }
                    }
                }
            }
        }
    }
}