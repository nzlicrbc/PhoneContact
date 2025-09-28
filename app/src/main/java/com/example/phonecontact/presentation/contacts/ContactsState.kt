package com.example.phonecontact.presentation.contacts

import com.example.phonecontact.domain.model.Contact
import com.example.phonecontact.domain.model.SearchHistory

data class ContactsState(
    val contacts: List<Contact> = emptyList(),
    val groupedContacts: Map<Char, List<Contact>> = emptyMap(),
    val searchQuery: String = "",
    val searchHistory: List<SearchHistory> = emptyList(),
    val isLoading: Boolean = false,
    val isSearchActive: Boolean = false,
    val error: String? = null
)