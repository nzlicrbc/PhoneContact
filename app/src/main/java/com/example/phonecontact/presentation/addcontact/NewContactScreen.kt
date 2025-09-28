package com.example.phonecontact.presentation.addcontact

import com.example.phonecontact.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.phonecontact.presentation.components.ContactAvatar
import com.example.phonecontact.utils.rememberImagePicker
import com.example.phonecontact.ui.theme.*
import com.example.phonecontact.ui.theme.Dimensions.CornerRadiusSmall
import com.example.phonecontact.ui.theme.Dimensions.IconSizeLarge
import com.example.phonecontact.ui.theme.Dimensions.PaddingHorizontal
import com.example.phonecontact.ui.theme.Dimensions.SpacerMedium
import com.example.phonecontact.ui.theme.Dimensions.SpacerSmall
import com.example.phonecontact.utils.Constants.CONTACT_SAVED_DELAY
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewContactScreen(
    onNavigateBack: () -> Unit,
    onContactSaved: () -> Unit,
    contactId: String? = null,
    isEditMode: Boolean = false,
    viewModel: NewContactViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val (launchCamera, launchGallery) = rememberImagePicker { uri, bytes ->
        uri?.let { viewModel.onEvent(NewContactEvent.ProfileImageSelected(it.toString(), bytes)) }
    }

    LaunchedEffect(contactId) {
        if (isEditMode && contactId != null) viewModel.loadContact(contactId)
    }

    LaunchedEffect(state.isContactSaved) {
        if (state.isContactSaved) {
            delay(CONTACT_SAVED_DELAY)
            onContactSaved()
            viewModel.onEvent(NewContactEvent.ContactSavedAcknowledged)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = if (isEditMode) stringResource(R.string.title_edit_contact) else stringResource(
                                R.string.title_new_contact
                            ),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    navigationIcon = {
                        Text(
                            text = stringResource(R.string.action_cancel),
                            color = Blue,
                            modifier = Modifier
                                .padding(start = Dimensions.PaddingHorizontal)
                                .clickable { onNavigateBack() },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    actions = {
                        Text(
                            text = stringResource(R.string.action_done),
                            color = if (state.isFormValid && !state.isSaving) Blue else Color.Gray,
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clickable(enabled = state.isFormValid && !state.isSaving) {
                                    viewModel.onEvent(NewContactEvent.SaveContact)
                                },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceWhite)
                )
            },
            containerColor = SurfaceWhite
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceWhite)
                        .padding(vertical = Dimensions.PaddingVerticalLarge),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .clickable { viewModel.onEvent(NewContactEvent.AddPhotoClicked) }
                        ) {
                            if (state.profileImageUri != null) {
                                ContactAvatar(
                                    imageUrl = state.profileImageUri,
                                    name = "${state.firstName} ${state.lastName}".trim()
                                        .ifEmpty { stringResource(R.string.avatar_fallback_name )},
                                    size = Dimensions.profileImageSizeLarge,
                                    enableColorShadow = true
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(Dimensions.profileImageSizeLarge)
                                        .clip(CircleShape)
                                        .background(Background),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = stringResource(R.string.content_description_add_photo),
                                        modifier = Modifier.size(IconSizeLarge),
                                        tint = TextSecondary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(SpacerSmall))

                        Text(
                            text = if (state.profileImageUri != null) stringResource(R.string.label_change_photo) else stringResource(R.string.label_add_photo),
                            style = MaterialTheme.typography.labelMedium,
                            color = Blue,
                            modifier = Modifier.clickable {
                                viewModel.onEvent(NewContactEvent.AddPhotoClicked)
                            }
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = PaddingHorizontal)
                ) {
                    OutlinedTextField(
                        value = state.firstName,
                        onValueChange = { viewModel.onEvent(NewContactEvent.FirstNameChanged(it)) },
                        placeholder = { Text(text = stringResource(R.string.placeholder_first_name)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(CornerRadiusSmall),
                        isError = state.firstNameError != null
                    )
                    state.firstNameError?.let { error ->
                        Text(
                            text = error,
                            style = MaterialTheme.typography.labelSmall,
                            color = DeleteRed,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(SpacerMedium))

                    OutlinedTextField(
                        value = state.lastName,
                        onValueChange = { viewModel.onEvent(NewContactEvent.LastNameChanged(it)) },
                        placeholder = { Text(text = stringResource(R.string.placeholder_last_name)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(CornerRadiusSmall),
                        isError = state.lastNameError != null
                    )
                    state.lastNameError?.let { error ->
                        Text(
                            text = error,
                            style = MaterialTheme.typography.labelSmall,
                            color = DeleteRed,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(SpacerMedium))

                    OutlinedTextField(
                        value = state.phoneNumber,
                        onValueChange = { viewModel.onEvent(NewContactEvent.PhoneNumberChanged(it)) },
                        placeholder = { Text(text = stringResource(R.string.placeholder_phone_number)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(CornerRadiusSmall),
                        isError = state.phoneNumberError != null
                    )
                    state.phoneNumberError?.let { error ->
                        Text(
                            text = error,
                            style = MaterialTheme.typography.labelSmall,
                            color = DeleteRed,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }
                }

                state.error?.let { error ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DeleteRed.copy(alpha = 0.1f))
                    ) {
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodyMedium,
                            color = DeleteRed,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        if (state.isContactSaved) {
            ContactSavedScreen(
                onDismiss = {
                    onContactSaved()
                    viewModel.onEvent(NewContactEvent.ContactSavedAcknowledged)
                }
            )
        }

        if (state.showPhotoSelectionSheet) {
            PhotoSelectionBottomSheet(
                onDismiss = { viewModel.onEvent(NewContactEvent.DismissPhotoSheet) },
                onCameraSelected = {
                    viewModel.onEvent(NewContactEvent.DismissPhotoSheet)
                    launchCamera()
                },
                onGallerySelected = {
                    viewModel.onEvent(NewContactEvent.DismissPhotoSheet)
                    launchGallery()
                }
            )
        }
    }
}