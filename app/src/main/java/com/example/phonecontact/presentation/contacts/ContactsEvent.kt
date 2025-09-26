package com.example.phonecontact.presentation.contacts

sealed class ContactsEvent {
    data object OnScreenAppeared : ContactsEvent()
    data class SearchQueryChanged(val query: String) : ContactsEvent()
    data class DeleteContact(val contactId: String) : ContactsEvent()
    data class NavigateToProfile(val contactId: String) : ContactsEvent()
    data object NavigateToAddContact : ContactsEvent()
    data object ClearSearchHistory : ContactsEvent()
    data class RemoveFromSearchHistory(val query: String) : ContactsEvent()
    data object RefreshContacts : ContactsEvent()
    data class OnSearchFocusChanged(val isFocused: Boolean) : ContactsEvent()
}