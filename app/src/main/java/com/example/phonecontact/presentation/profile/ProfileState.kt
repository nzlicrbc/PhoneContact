package com.example.phonecontact.presentation.profile

import com.example.phonecontact.domain.model.Contact

data class ProfileState(
    val contact: Contact? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDeleteDialog: Boolean = false,
    val showMenu: Boolean = false,
    val isDeleting: Boolean = false,
    val isDeleteSuccess: Boolean = false,
    val isSavedToDevice: Boolean = false
)