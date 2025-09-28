package com.example.phonecontact.presentation.addcontact

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.*
import com.example.phonecontact.R
import com.example.phonecontact.utils.rememberImagePicker
import com.example.phonecontact.ui.theme.*

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
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.done))
    val lottieProgress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = state.isSaving,
        iterations = LottieConstants.IterateForever
    )
    val (launchCamera, launchGallery) = rememberImagePicker { uri, bytes ->
        uri?.let { viewModel.onEvent(NewContactEvent.ProfileImageSelected(it.toString(), bytes)) }
    }

    LaunchedEffect(contactId) {
        if (isEditMode && contactId != null) viewModel.loadContact(contactId)
    }

    LaunchedEffect(state.isContactSaved) {
        if (state.isContactSaved) {
            onContactSaved()
            viewModel.onEvent(NewContactEvent.ContactSavedAcknowledged)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditMode) "Edit Contact" else "New Contact",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    Text(
                        text = "Cancel",
                        color = Blue,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable { onNavigateBack() },
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                actions = {
                    Text(
                        text = "Done",
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
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceWhite)
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(Dimensions.profileImageSizeLarge)
                                .clip(CircleShape)
                                .background(Background)
                                .clickable { viewModel.onEvent(NewContactEvent.AddPhotoClicked) },
                            contentAlignment = Alignment.Center
                        ) {
                            if (state.profileImageUri != null) {
                                AsyncImage(
                                    model = state.profileImageUri,
                                    contentDescription = "Profile Photo",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Add Photo",
                                    modifier = Modifier.size(60.dp),
                                    tint = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (state.profileImageUri != null) "Change Photo" else "Add Photo",
                            style = MaterialTheme.typography.labelMedium,
                            color = Blue,
                            modifier = Modifier.clickable {
                                viewModel.onEvent(NewContactEvent.AddPhotoClicked)
                            }
                        )
                    }
                }

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)) {
                    OutlinedTextField(
                        value = state.firstName,
                        onValueChange = { viewModel.onEvent(NewContactEvent.FirstNameChanged(it)) },
                        placeholder = { Text("First Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
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

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = state.lastName,
                        onValueChange = { viewModel.onEvent(NewContactEvent.LastNameChanged(it)) },
                        placeholder = { Text("Last Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
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

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = state.phoneNumber,
                        onValueChange = { viewModel.onEvent(NewContactEvent.PhoneNumberChanged(it)) },
                        placeholder = { Text("Phone Number") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
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

            if (state.isSaving) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.size(200.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            LottieAnimation(
                                composition = composition,
                                progress = { lottieProgress },
                                modifier = Modifier.size(100.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Saving contact...",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
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