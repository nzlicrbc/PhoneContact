package com.example.phonecontact.presentation.profile

sealed class ProfileEvent {
    data class LoadContact(val contactId: String) : ProfileEvent()
    data object ShowMenu : ProfileEvent()
    data object HideMenu : ProfileEvent()
    data object EditClicked : ProfileEvent()
    data object DeleteClicked : ProfileEvent()
    data object ConfirmDelete : ProfileEvent()
    data object CancelDelete : ProfileEvent()
    data object SaveToDevice : ProfileEvent()
    data object NavigateBack : ProfileEvent()
}