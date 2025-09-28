package com.example.phonecontact.presentation.profile

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.phonecontact.R
import com.example.phonecontact.presentation.components.ContactAvatar
import com.example.phonecontact.presentation.components.LoadingIndicator
import com.example.phonecontact.ui.theme.*
import com.example.phonecontact.utils.Constants

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showDeleteBottomSheet by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.onEvent(ProfileEvent.SaveToDevice)
        }
    }

    LaunchedEffect(state.contact?.profileImageUrl) {
        println("Profile Avatar URL: ${state.contact?.profileImageUrl}")
    }

    LaunchedEffect(state.isDeleteSuccess) {
        if (state.isDeleteSuccess) {
            onNavigateBack()
        }
    }

    LaunchedEffect(state.showDeleteDialog) {
        showDeleteBottomSheet = state.showDeleteDialog
    }

    Box {
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
                                    .padding(
                                        horizontal = Dimensions.paddingLarge,
                                        vertical = Dimensions.paddingLarge
                                    ),
                                contentAlignment = Alignment.TopEnd
                            ) {
                                IconButton(
                                    onClick = { viewModel.onEvent(ProfileEvent.ShowMenu) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = stringResource(R.string.more_options),
                                        tint = TextPrimary
                                    )
                                }

                                Box(
                                    modifier = Modifier.offset(
                                        x = (-Dimensions.dropdownMenuOffset),
                                        y = Dimensions.dropdownMenuTopOffset
                                    )
                                ) {
                                    DropdownMenu(
                                        expanded = state.showMenu,
                                        onDismissRequest = { viewModel.onEvent(ProfileEvent.HideMenu) },
                                        modifier = Modifier
                                            .background(
                                                color = SurfaceWhite,
                                                shape = RoundedCornerShape(Dimensions.cornerRadiusDropdown)
                                            )
                                            .width(Dimensions.dropdownMenuWidth)
                                    ) {
                                        DropdownMenuItem(
                                            text = {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(stringResource(R.string.edit), color = TextPrimary)
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.pencil),
                                                        contentDescription = stringResource(R.string.edit),
                                                        modifier = Modifier.size(Dimensions.iconDefault),
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
                                            thickness = Dimensions.borderWidthThin,
                                            modifier = Modifier.padding(horizontal = Dimensions.paddingMedium)
                                        )

                                        DropdownMenuItem(
                                            text = {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(stringResource(R.string.delete), color = DeleteRed)
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.delete),
                                                        contentDescription = stringResource(R.string.delete),
                                                        modifier = Modifier.size(Dimensions.iconDefault),
                                                        tint = DeleteRed
                                                    )
                                                }
                                            },
                                            onClick = {
                                                viewModel.onEvent(ProfileEvent.DeleteClicked)
                                                viewModel.onEvent(ProfileEvent.HideMenu)
                                            }
                                        )
                                    }
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = Dimensions.spacingSmall),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                ContactAvatar(
                                    imageUrl = state.contact!!.profileImageUrl,
                                    name = state.contact!!.firstName,
                                    size = Dimensions.profileImageSizeLarge,
                                    enableColorShadow = true
                                )

                                Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

                                Text(
                                    text = if (state.contact!!.profileImageUrl != null)
                                        stringResource(R.string.change_photo)
                                    else
                                        stringResource(R.string.add_photo),
                                    color = Blue,
                                    fontSize = TextSizes.small,
                                    modifier = Modifier.clickable {
                                        state.contact?.let { onNavigateToEdit(it.id) }
                                    }
                                )
                            }

                            Spacer(modifier = Modifier.height(Dimensions.paddingXLarge))

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = Dimensions.paddingLarge)
                            ) {
                                OutlinedTextField(
                                    value = state.contact!!.firstName,
                                    onValueChange = { },
                                    placeholder = { Text(stringResource(R.string.first_name)) },
                                    singleLine = true,
                                    readOnly = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(Dimensions.cornerRadiusTextField),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        disabledBorderColor = DividerColor,
                                        unfocusedBorderColor = DividerColor
                                    )
                                )

                                Spacer(modifier = Modifier.height(Dimensions.paddingLarge))

                                OutlinedTextField(
                                    value = state.contact!!.lastName,
                                    onValueChange = { },
                                    placeholder = { Text(stringResource(R.string.last_name)) },
                                    singleLine = true,
                                    readOnly = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(Dimensions.cornerRadiusTextField),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        disabledBorderColor = DividerColor,
                                        unfocusedBorderColor = DividerColor
                                    )
                                )

                                Spacer(modifier = Modifier.height(Dimensions.paddingLarge))

                                OutlinedTextField(
                                    value = state.contact!!.phoneNumber,
                                    onValueChange = { },
                                    placeholder = { Text(stringResource(R.string.phone_number)) },
                                    singleLine = true,
                                    readOnly = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(Dimensions.cornerRadiusTextField),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        disabledBorderColor = DividerColor,
                                        unfocusedBorderColor = DividerColor
                                    )
                                )

                                Spacer(modifier = Modifier.height(Dimensions.paddingXXLarge))

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
                                        .height(Dimensions.buttonHeightMedium),
                                    shape = RoundedCornerShape(Dimensions.cornerRadiusLarge),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = if (state.isSavedToDevice) TextSecondary else TextPrimary,
                                        containerColor = Color.Transparent
                                    ),
                                    border = ButtonDefaults.outlinedButtonBorder.copy(
                                        width = Dimensions.borderWidthSmall
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
                                            modifier = Modifier.size(Dimensions.iconMedium),
                                            tint = if (state.isSavedToDevice) TextSecondary else TextPrimary
                                        )
                                        Spacer(modifier = Modifier.width(Dimensions.spacingSmall))
                                        Text(
                                            text = stringResource(R.string.save_to_phone),
                                            fontSize = TextSizes.small
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showDeleteBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showDeleteBottomSheet = false
                    viewModel.onEvent(ProfileEvent.CancelDelete)
                },
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
                        text = stringResource(R.string.delete_contact_confirmation),
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
                            onClick = {
                                showDeleteBottomSheet = false
                                viewModel.onEvent(ProfileEvent.CancelDelete)
                            },
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
                                showDeleteBottomSheet = false
                                viewModel.onEvent(ProfileEvent.ConfirmDelete)
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