package com.example.phonecontact.presentation.addcontact

import com.example.phonecontact.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.phonecontact.utils.rememberImagePicker
import com.example.phonecontact.presentation.components.CustomTextField
import com.example.phonecontact.presentation.components.CustomTextButton
import com.example.phonecontact.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewContactScreen(
    onNavigateBack: () -> Unit,
    onContactSaved: () -> Unit,
    viewModel: NewContactViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.done)
    )
    val lottieProgress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = state.isSaving,
        iterations = LottieConstants.IterateForever
    )

    val (launchCamera, launchGallery) = rememberImagePicker { uri, bytes ->
        uri?.let {
            viewModel.onEvent(NewContactEvent.ProfileImageSelected(it.toString(), bytes))
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
                        text = "New Contact",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    CustomTextButton(
                        text = "Cancel",
                        onClick = onNavigateBack,
                        textColor = Blue
                    )
                },
                actions = {
                    CustomTextButton(
                        text = "Done",
                        onClick = { viewModel.onEvent(NewContactEvent.SaveContact) },
                        enabled = state.isFormValid && !state.isSaving,
                        textColor = Blue
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SurfaceWhite
                )
            )
        },
        containerColor = Background
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceWhite)
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Change Photo",
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Blue)
                                        .padding(8.dp),
                                    tint = SurfaceWhite
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Photo",
                                    modifier = Modifier.size(40.dp),
                                    tint = Blue
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

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceWhite)
                        .padding(horizontal = Dimensions.paddingMedium)
                ) {
                    CustomTextField(
                        value = state.firstName,
                        onValueChange = { viewModel.onEvent(NewContactEvent.FirstNameChanged(it)) },
                        placeholder = "First Name",
                        modifier = Modifier.fillMaxWidth(),
                        isError = state.firstNameError != null
                    )

                    state.firstNameError?.let { error ->
                        Text(
                            text = error,
                            style = MaterialTheme.typography.labelSmall,
                            color = DeleteRed,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomTextField(
                        value = state.lastName,
                        onValueChange = { viewModel.onEvent(NewContactEvent.LastNameChanged(it)) },
                        placeholder = "Last Name",
                        modifier = Modifier.fillMaxWidth(),
                        isError = state.lastNameError != null
                    )

                    state.lastNameError?.let { error ->
                        Text(
                            text = error,
                            style = MaterialTheme.typography.labelSmall,
                            color = DeleteRed,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomTextField(
                        value = state.phoneNumber,
                        onValueChange = { viewModel.onEvent(NewContactEvent.PhoneNumberChanged(it)) },
                        placeholder = "Phone Number",
                        modifier = Modifier.fillMaxWidth(),
                        isError = state.phoneNumberError != null
                    )

                    state.phoneNumberError?.let { error ->
                        Text(
                            text = error,
                            style = MaterialTheme.typography.labelSmall,
                            color = DeleteRed,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                state.error?.let { error ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimensions.paddingMedium),
                        colors = CardDefaults.cardColors(
                            containerColor = DeleteRed.copy(alpha = 0.1f)
                        )
                    ) {
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodyMedium,
                            color = DeleteRed,
                            modifier = Modifier.padding(Dimensions.paddingMedium),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            if (state.isSaving) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(TextPrimary.copy(alpha = 0.5f)),
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
                        }
                    }
                }
            }
        }

        if (state.showPhotoSelectionSheet) {
            PhotoSelectionBottomSheet(
                onDismiss = { viewModel.onEvent(NewContactEvent.DismissPhotoSheet) },
                onCameraSelected = { viewModel.onEvent(NewContactEvent.CameraSelected) },
                onGallerySelected = { viewModel.onEvent(NewContactEvent.GallerySelected) }
            )
        }

        if (state.isSaving) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(TextPrimary.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Blue)
            }
        }
    }
}