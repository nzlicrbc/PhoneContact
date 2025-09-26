package com.example.phonecontact.presentation.contacts

import com.example.phonecontact.domain.model.Contact

data class ContactsState(
    val contacts: List<Contact> = emptyList(),
    val groupedContacts: Map<Char, List<Contact>> = emptyMap(),
    val searchQuery: String = "",
    val searchHistory: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSearchActive: Boolean = false
)