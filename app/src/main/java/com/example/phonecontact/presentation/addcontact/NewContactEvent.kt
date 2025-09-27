package com.example.phonecontact.presentation.addcontact

sealed class NewContactEvent {
    data class FirstNameChanged(val firstName: String) : NewContactEvent()
    data class LastNameChanged(val lastName: String) : NewContactEvent()
    data class PhoneNumberChanged(val phoneNumber: String) : NewContactEvent()
    data class ProfileImageSelected(val uri: String) : NewContactEvent()
    data object SaveContact : NewContactEvent()
    data object CancelClicked : NewContactEvent()
    data object AddPhotoClicked : NewContactEvent()
    data object DismissPhotoSheet : NewContactEvent()
    data object CameraSelected : NewContactEvent()
    data object GallerySelected : NewContactEvent()
    data object ContactSavedAcknowledged : NewContactEvent()
}