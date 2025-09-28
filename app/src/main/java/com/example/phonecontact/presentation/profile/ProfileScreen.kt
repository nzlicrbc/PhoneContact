package com.example.phonecontact.presentation.profile

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import com.example.phonecontact.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.phonecontact.MainActivity
import com.example.phonecontact.presentation.components.ContactAvatar
import com.example.phonecontact.presentation.components.LoadingIndicator
import com.example.phonecontact.ui.theme.*

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.onEvent(ProfileEvent.SaveToDevice)
        }
    }

    LaunchedEffect(state.isDeleteSuccess) {
        if (state.isDeleteSuccess) {
            onNavigateBack()
        }
    }

    Scaffold(
        containerColor = SurfaceWhite
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    LoadingIndicator()
                }

                state.contact != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 16.dp),
                            contentAlignment = Alignment.TopEnd
                        ) {
                            IconButton(
                                onClick = { viewModel.onEvent(ProfileEvent.ShowMenu) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More options",
                                    tint = TextPrimary
                                )
                            }

                            Box(
                                modifier = Modifier.offset(x = (-8).dp, y = 40.dp)
                            ) {
                                DropdownMenu(
                                    expanded = state.showMenu,
                                    onDismissRequest = { viewModel.onEvent(ProfileEvent.HideMenu) },
                                    modifier = Modifier
                                        .background(
                                            color = SurfaceWhite,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .width(150.dp)
                                ) {
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text("Edit", color = TextPrimary)
                                                Icon(
                                                    painter = painterResource(id = R.drawable.pencil),
                                                    contentDescription = "Edit",
                                                    modifier = Modifier.size(20.dp),
                                                    tint = TextPrimary
                                                )
                                            }
                                        },
                                        onClick = {
                                            state.contact?.let { onNavigateToEdit(it.id) }
                                            viewModel.onEvent(ProfileEvent.HideMenu)
                                        }
                                    )

                                    Divider(
                                        color = DividerColor,
                                        thickness = 0.5.dp,
                                        modifier = Modifier.padding(horizontal = 12.dp)
                                    )

                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text("Delete", color = DeleteRed)
                                                Icon(
                                                    painter = painterResource(id = R.drawable.delete),
                                                    contentDescription = "Delete",
                                                    modifier = Modifier.size(20.dp),
                                                    tint = DeleteRed
                                                )
                                            }
                                        },
                                        onClick = { viewModel.onEvent(ProfileEvent.DeleteClicked) }
                                    )
                                }
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ContactAvatar(
                                imageUrl = state.contact!!.profileImageUrl,
                                name = state.contact!!.firstName,
                                size = Dimensions.profileImageSizeLarge
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = if (state.contact!!.profileImageUrl != null) "Change Photo" else "Add Photo",
                                color = Blue,
                                fontSize = 15.sp,
                                modifier = Modifier.clickable {
                                    state.contact?.let { onNavigateToEdit(it.id) }
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            OutlinedTextField(
                                value = state.contact!!.firstName,
                                onValueChange = { },
                                placeholder = { Text("First Name") },
                                singleLine = true,
                                readOnly = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledBorderColor = DividerColor,
                                    unfocusedBorderColor = DividerColor
                                )
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = state.contact!!.lastName,
                                onValueChange = { },
                                placeholder = { Text("Last Name") },
                                singleLine = true,
                                readOnly = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledBorderColor = DividerColor,
                                    unfocusedBorderColor = DividerColor
                                )
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = state.contact!!.phoneNumber,
                                onValueChange = { },
                                placeholder = { Text("Phone Number") },
                                singleLine = true,
                                readOnly = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledBorderColor = DividerColor,
                                    unfocusedBorderColor = DividerColor
                                )
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            OutlinedButton(
                                onClick = {
                                    if (ContextCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.WRITE_CONTACTS
                                        ) != PackageManager.PERMISSION_GRANTED
                                    ) {
                                        permissionLauncher.launch(Manifest.permission.WRITE_CONTACTS)
                                    } else {
                                        viewModel.onEvent(ProfileEvent.SaveToDevice)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(24.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = if (state.isSavedToDevice) TextSecondary else TextPrimary,
                                    containerColor = Color.Transparent
                                ),
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    width = 1.dp
                                ),
                                enabled = !state.isSavedToDevice
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.save),
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp),
                                        tint = if (state.isSavedToDevice) TextSecondary else TextPrimary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Save to My Phone Contact",
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (state.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(ProfileEvent.CancelDelete) },
            title = { Text("Delete Contact") },
            text = { Text("Are you sure you want to delete this contact?") },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.onEvent(ProfileEvent.ConfirmDelete) }
                ) {
                    Text("Yes", color = DeleteRed)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.onEvent(ProfileEvent.CancelDelete) }
                ) {
                    Text("No")
                }
            }
        )
    }
}