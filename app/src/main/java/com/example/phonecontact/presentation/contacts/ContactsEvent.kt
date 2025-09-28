package com.example.phonecontact.presentation.contacts

sealed class ContactsEvent {
    object OnScreenAppeared : ContactsEvent()
    data class SearchQueryChanged(val query: String) : ContactsEvent()
    data class OnSearchFocusChanged(val isFocused: Boolean) : ContactsEvent()
    data class DeleteContact(val contactId: String) : ContactsEvent()
    data class AddToSearchHistory(val query: String) : ContactsEvent()
    data class RemoveFromSearchHistory(val query: String) : ContactsEvent()
    object ClearSearchHistory : ContactsEvent()
    object RefreshContacts : ContactsEvent()
}