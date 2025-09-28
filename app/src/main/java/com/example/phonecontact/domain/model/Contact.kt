package com.example.phonecontact.domain.model

data class Contact(
    val id: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val profileImageUrl: String? = null,
    val isInDeviceContacts: Boolean = false,
    val createdAt: String? = null
) {
    val fullName: String
        get() = "$firstName $lastName"

    val initials: String
        get() = "${firstName.firstOrNull()?.uppercase().orEmpty()}${lastName.firstOrNull()?.uppercase().orEmpty()}"

    val firstLetter: String
        get() = firstName.firstOrNull()?.uppercase().orEmpty()
}