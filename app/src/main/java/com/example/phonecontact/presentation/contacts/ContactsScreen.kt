package com.example.phonecontact.presentation.contacts

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.phonecontact.domain.model.Contact
import com.example.phonecontact.presentation.components.*
import com.example.phonecontact.ui.theme.*

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(
    onNavigateToAddContact: () -> Unit,
    onNavigateToEditContact: (String) -> Unit,
    viewModel: ContactsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var selectedContact by remember { mutableStateOf<Contact?>(null) }

    LaunchedEffect(key1 = true) {
        viewModel.onEvent(ContactsEvent.OnScreenAppeared)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Contacts",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToAddContact) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Contact",
                            tint = Blue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SurfaceWhite
                )
            )
        },
        containerColor = Background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimensions.paddingMedium)
                    .padding(top = 8.dp, bottom = 8.dp)
            ) {
                SearchBar(
                    query = state.searchQuery,
                    onQueryChange = {
                        viewModel.onEvent(ContactsEvent.SearchQueryChanged(it))
                    },
                    onFocusChange = {
                        viewModel.onEvent(ContactsEvent.OnSearchFocusChanged(it))
                    }
                )
            }

            when {
                state.isLoading -> {
                    LoadingIndicator()
                }
                state.contacts.isEmpty() && state.searchQuery.isEmpty() -> {
                    EmptyState(
                        title = "No Contacts",
                        message = "Contacts you've added will appear here.",
                        buttonText = "Create New Contact",
                        onButtonClick = onNavigateToAddContact
                    )
                }
                state.contacts.isEmpty() && state.searchQuery.isNotEmpty() -> {
                    EmptyState(
                        title = "No Results",
                        message = "The user you are looking for could not be found."
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        state.groupedContacts.forEach { (letter, contactsForLetter) ->
                            item(key = "header_$letter") {
                                SectionHeader(letter = letter.toString())
                            }

                            items(
                                count = contactsForLetter.size,
                                key = { index -> contactsForLetter[index].id }
                            ) { index ->
                                val contact = contactsForLetter[index]
                                val isLast = index == contactsForLetter.size - 1

                                SwipeableContactItem(
                                    contact = contact,
                                    onClick = {
                                        selectedContact = contact
                                    },
                                    onEdit = {
                                        onNavigateToEditContact(contact.id)
                                    },
                                    onDelete = {
                                        viewModel.onEvent(ContactsEvent.DeleteContact(contact.id))
                                    },
                                    showDivider = !isLast,
                                    isInDeviceContacts = contact.isInDeviceContacts
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}