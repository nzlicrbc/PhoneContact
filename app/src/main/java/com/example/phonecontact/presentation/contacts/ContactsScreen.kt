package com.example.phonecontact.presentation.contacts

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.phonecontact.R
import com.example.phonecontact.presentation.components.*
import com.example.phonecontact.ui.theme.*
import com.example.phonecontact.utils.Constants

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(
    onNavigateToAddContact: () -> Unit,
    onNavigateToProfile: (String) -> Unit,
    onNavigateToEditContact: (String) -> Unit,
    viewModel: ContactsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.onEvent(ContactsEvent.OnScreenAppeared)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.contacts_title),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToAddContact) {
                        Box(
                            modifier = Modifier
                                .size(Dimensions.fabSizeSmall)
                                .background(Blue, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.add_contact),
                                tint = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background
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
                    .padding(
                        top = Dimensions.searchBarVerticalSpacing,
                        bottom = Dimensions.searchBarVerticalSpacing
                    )
            ) {
                SearchBar(
                    query = state.searchQuery,
                    onQueryChange = {
                        viewModel.onEvent(ContactsEvent.SearchQueryChanged(it))
                    },
                    onFocusChange = {
                        viewModel.onEvent(ContactsEvent.OnSearchFocusChanged(it))
                    },
                    searchHistory = state.searchHistory,
                    onHistoryItemClick = { query ->
                        viewModel.onEvent(ContactsEvent.SearchQueryChanged(query))
                        viewModel.onEvent(ContactsEvent.AddToSearchHistory(query))
                    },
                    onClearHistory = {
                        viewModel.onEvent(ContactsEvent.ClearSearchHistory)
                    }
                )
            }

            when {
                state.isLoading -> {
                    LoadingIndicator()
                }
                state.contacts.isEmpty() && state.searchQuery.isEmpty() -> {
                    EmptyState(
                        title = stringResource(R.string.no_contacts_title),
                        message = stringResource(R.string.no_contacts_message),
                        buttonText = stringResource(R.string.create_new_contact),
                        onButtonClick = onNavigateToAddContact
                    )
                }
                state.contacts.isEmpty() && state.searchQuery.isNotEmpty() -> {
                    EmptyState(
                        title = stringResource(R.string.no_results_title),
                        message = stringResource(R.string.no_results_message)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = Dimensions.paddingLarge)
                    ) {
                        state.groupedContacts.forEach { (letter, contactsForLetter) ->
                            item(key = "${Constants.SECTION_KEY_PREFIX}$letter") {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            horizontal = Dimensions.paddingMedium,
                                            vertical = Dimensions.paddingSmall
                                        ),
                                    colors = CardDefaults.cardColors(
                                        containerColor = SurfaceWhite
                                    ),
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = Dimensions.elevationNone
                                    )
                                ) {
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            text = letter.toString().uppercase(),
                                            style = MaterialTheme.typography.labelLarge,
                                            color = TextSecondary,
                                            modifier = Modifier
                                                .padding(
                                                    horizontal = Dimensions.paddingMedium,
                                                    vertical = Dimensions.paddingSmall
                                                )
                                        )

                                        DividerLine(modifier = Modifier.fillMaxWidth())

                                        contactsForLetter.forEachIndexed { index, contact ->
                                            val isLast = index == contactsForLetter.size - 1

                                            SwipeableContactItem(
                                                contact = contact,
                                                onClick = { onNavigateToProfile(contact.id) },
                                                onEdit = { onNavigateToEditContact(contact.id) },
                                                onDelete = {
                                                    viewModel.onEvent(ContactsEvent.DeleteContact(contact.id))
                                                },
                                                showDivider = !isLast
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}