package com.example.phonecontact.presentation.addcontact

data class NewContactState(
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val profileImageUri: String? = null,
    val profileImageUrl: String? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val isContactSaved: Boolean = false,
    val showPhotoSelectionSheet: Boolean = false,
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val phoneNumberError: String? = null
) {
    val isFormValid: Boolean
        get() = firstName.isNotBlank() &&
                lastName.isNotBlank() &&
                phoneNumber.isNotBlank() &&
                phoneNumber.length >= 10 &&
                firstNameError == null &&
                lastNameError == null &&
                phoneNumberError == null
}