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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.phonecontact.domain.model.Contact
import com.example.phonecontact.ui.theme.*

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
                                .width(72.dp)
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
                                contentDescription = "Edit",
                                tint = Color.White
                            )
                        }

                        Box(
                            modifier = Modifier
                                .width(72.dp)
                                .fillMaxHeight()
                                .background(DeleteRed)
                                .clickable {
                                    showDeleteBottomSheet = true
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
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
                containerColor = Color.White,
                dragHandle = {
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .background(Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(2.dp))
                    )
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Delete Contact",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Are you sure you want to delete ${contact.firstName} ${contact.lastName}?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showDeleteBottomSheet = false },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            ),
                            border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f)),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text("No")
                        }

                        Button(
                            onClick = {
                                onDelete()
                                showDeleteBottomSheet = false
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text("Yes")
                        }
                    }
                }
            }
        }
    }
}