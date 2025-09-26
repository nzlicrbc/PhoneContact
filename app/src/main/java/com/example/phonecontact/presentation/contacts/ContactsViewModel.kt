package com.example.phonecontact.presentation.contacts

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonecontact.domain.model.Contact
import com.example.phonecontact.domain.usecase.DeleteContactUseCase
import com.example.phonecontact.domain.usecase.GetContactUseCase
import com.example.phonecontact.domain.usecase.SearchContactUseCase
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
            is ContactsEvent.SearchQueryChanged -> {
                updateSearchQuery(event.query)
            }
            is ContactsEvent.DeleteContact -> {
                deleteContact(event.contactId)
            }
            is ContactsEvent.ClearSearchHistory -> {
                clearSearchHistory()
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
            else -> {}
        }
    }

    private fun loadContacts() {
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
            addToSearchHistory(query)
        } else {
            loadContacts()
        }
    }

    private fun searchContacts(query: String) {
        viewModelScope.launch {
            searchContactUseCase(query).collect { results ->
                val grouped = groupContactsByFirstLetter(results)
                _state.update {
                    it.copy(
                        contacts = results,
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
                contact.firstName.firstOrNull()?.uppercaseChar() ?: '#'
            }
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    private fun addToSearchHistory(query: String) {
        val history = _state.value.searchHistory.toMutableList()
        history.remove(query)
        history.add(0, query)
        if (history.size > 10) {
            history.removeLast()
        }
        _state.update { it.copy(searchHistory = history) }
    }

    private fun clearSearchHistory() {
        _state.update { it.copy(searchHistory = emptyList()) }
    }

    private fun removeFromSearchHistory(query: String) {
        val history = _state.value.searchHistory.toMutableList()
        history.remove(query)
        _state.update { it.copy(searchHistory = history) }
    }
}