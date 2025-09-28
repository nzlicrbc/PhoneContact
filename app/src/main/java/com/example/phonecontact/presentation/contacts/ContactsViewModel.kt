package com.example.phonecontact.presentation.contacts

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonecontact.domain.model.Contact
import com.example.phonecontact.domain.model.SearchHistory
import com.example.phonecontact.domain.usecase.DeleteContactUseCase
import com.example.phonecontact.domain.usecase.GetContactUseCase
import com.example.phonecontact.domain.usecase.SearchContactUseCase
import com.example.phonecontact.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val getContactUseCase: GetContactUseCase,
    private val deleteContactUseCase: DeleteContactUseCase,
    private val searchContactUseCase: SearchContactUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ContactsState())
    val state: StateFlow<ContactsState> = _state.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun onEvent(event: ContactsEvent) {
        when (event) {
            is ContactsEvent.OnScreenAppeared -> {
                loadContacts()
            }
            is ContactsEvent.SearchQueryChanged -> {
                updateSearchQuery(event.query)
            }
            is ContactsEvent.DeleteContact -> {
                deleteContact(event.contactId)
            }
            is ContactsEvent.RemoveFromSearchHistory -> {
                removeFromSearchHistory(event.query)
            }
            ContactsEvent.RefreshContacts -> {
                loadContacts()
            }
            is ContactsEvent.OnSearchFocusChanged -> {
                _state.update { it.copy(isSearchActive = event.isFocused) }
            }
            is ContactsEvent.AddToSearchHistory -> {
                if (event.query.isNotBlank() && event.query.length >= Constants.MIN_SEARCH_QUERY_LENGTH) {
                    addToSearchHistory(event.query)
                }
            }
            is ContactsEvent.ClearSearchHistory -> {
                clearSearchHistory()
            }
        }
    }

    fun loadContacts() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            getContactUseCase().collect { contacts ->
                val grouped = groupContactsByFirstLetter(contacts)
                _state.update {
                    it.copy(
                        contacts = contacts,
                        groupedContacts = grouped,
                        isLoading = false
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    private fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }

        if (query.isNotEmpty()) {
            searchContacts(query)
        } else {
            loadContacts()
        }
    }

    private fun searchContacts(query: String) {
        viewModelScope.launch {
            val normalizedQuery = query.trim().lowercase()

            getContactUseCase().collect { allContacts ->
                val filteredContacts = allContacts.filter { contact ->
                    val fullName = "${contact.firstName} ${contact.lastName}".lowercase()
                    val reverseName = "${contact.lastName} ${contact.firstName}".lowercase()

                    fullName.contains(normalizedQuery) ||
                            reverseName.contains(normalizedQuery) ||
                            contact.phoneNumber.contains(normalizedQuery)
                }

                val grouped = groupContactsByFirstLetter(filteredContacts)
                _state.update {
                    it.copy(
                        contacts = filteredContacts,
                        groupedContacts = grouped
                    )
                }
            }
        }
    }

    private fun deleteContact(contactId: String) {
        viewModelScope.launch {
            deleteContactUseCase(contactId)
            loadContacts()
        }
    }

    private fun groupContactsByFirstLetter(contacts: List<Contact>): Map<Char, List<Contact>> {
        return contacts
            .sortedBy { it.firstName.uppercase() }
            .groupBy { contact ->
                contact.firstName.firstOrNull()?.uppercaseChar() ?: Constants.DEFAULT_GROUP_CHAR
            }
    }

    private fun addToSearchHistory(query: String) {
        viewModelScope.launch {
            try {
                val currentHistory = _state.value.searchHistory.toMutableList()

                currentHistory.removeAll { it.searchQuery.equals(query, ignoreCase = true) }

                currentHistory.add(0, SearchHistory(
                    id = System.currentTimeMillis(),
                    searchQuery = query,
                    searchedAt = System.currentTimeMillis()
                ))

                val limitedHistory = currentHistory.take(Constants.MAX_SEARCH_HISTORY_STORAGE)

                _state.update { it.copy(searchHistory = limitedHistory) }

            } catch (e: Exception) {
            }
        }
    }

    private fun clearSearchHistory() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(searchHistory = emptyList()) }
            } catch (e: Exception) {
            }
        }
    }

    private fun removeFromSearchHistory(query: String) {
        viewModelScope.launch {
            try {
                val updatedHistory = _state.value.searchHistory.filter {
                    !it.searchQuery.equals(query, ignoreCase = true)
                }
                _state.update { it.copy(searchHistory = updatedHistory) }
            } catch (e: Exception) {
            }
        }
    }
}