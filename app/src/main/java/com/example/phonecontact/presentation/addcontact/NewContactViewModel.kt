package com.example.phonecontact.presentation.addcontact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonecontact.domain.repository.ContactRepository
import com.example.phonecontact.domain.usecase.CreateContactUseCase
import com.example.phonecontact.domain.usecase.UpdateContactUseCase
import com.example.phonecontact.domain.usecase.UploadImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewContactViewModel @Inject constructor(
    private val createContactUseCase: CreateContactUseCase,
    private val updateContactUseCase: UpdateContactUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
    private val repository: ContactRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NewContactState())
    val state: StateFlow<NewContactState> = _state.asStateFlow()

    private var editingContactId: String? = null

    fun loadContact(contactId: String) {
        editingContactId = contactId
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val contact = repository.getContactById(contactId)
                contact?.let {
                    _state.update { state ->
                        state.copy(
                            firstName = it.firstName,
                            lastName = it.lastName,
                            phoneNumber = it.phoneNumber,
                            profileImageUrl = it.profileImageUrl,
                            profileImageUri = it.profileImageUrl,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to load contact"
                    )
                }
            }
        }
    }

    fun onEvent(event: NewContactEvent) {
        when (event) {
            is NewContactEvent.FirstNameChanged -> {
                _state.update {
                    it.copy(
                        firstName = event.firstName,
                        firstNameError = validateFirstName(event.firstName)
                    )
                }
            }
            is NewContactEvent.LastNameChanged -> {
                _state.update {
                    it.copy(
                        lastName = event.lastName,
                        lastNameError = validateLastName(event.lastName)
                    )
                }
            }
            is NewContactEvent.PhoneNumberChanged -> {
                val formattedNumber = formatPhoneNumber(event.phoneNumber)
                _state.update {
                    it.copy(
                        phoneNumber = formattedNumber,
                        phoneNumberError = validatePhoneNumber(formattedNumber)
                    )
                }
            }
            is NewContactEvent.ProfileImageSelected -> {
                _state.update {
                    it.copy(
                        profileImageUri = event.uri,
                        profileImageBytes = event.imageBytes,
                        error = null
                    )
                }
                event.imageBytes?.let { uploadImage(it) }
            }
            NewContactEvent.SaveContact -> {
                if (editingContactId != null) {
                    updateContact()
                } else {
                    saveContact()
                }
            }
            NewContactEvent.AddPhotoClicked -> {
                _state.update { it.copy(showPhotoSelectionSheet = true) }
            }
            NewContactEvent.DismissPhotoSheet -> {
                _state.update { it.copy(showPhotoSelectionSheet = false) }
            }
            NewContactEvent.ContactSavedAcknowledged -> {
                _state.update { it.copy(isContactSaved = false) }
            }
            else -> {}
        }
    }

    private fun saveContact() {
        if (!_state.value.isFormValid) return

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null) }

            try {
                delay(2000)

                val result = createContactUseCase(
                    firstName = _state.value.firstName,
                    lastName = _state.value.lastName,
                    phoneNumber = _state.value.phoneNumber,
                    profileImageUrl = _state.value.profileImageUrl
                )

                if (result.isSuccess) {
                    _state.update {
                        it.copy(
                            isSaving = false,
                            isContactSaved = true
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            isSaving = false,
                            error = result.exceptionOrNull()?.message ?: "Failed to save contact"
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isSaving = false,
                        error = e.message ?: "An error occurred"
                    )
                }
            }
        }
    }

    private fun updateContact() {
        if (!_state.value.isFormValid || editingContactId == null) return

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null) }

            try {
                delay(2000)

                val contact = com.example.phonecontact.domain.model.Contact(
                    id = editingContactId!!,
                    firstName = _state.value.firstName,
                    lastName = _state.value.lastName,
                    phoneNumber = _state.value.phoneNumber,
                    profileImageUrl = _state.value.profileImageUrl
                )

                val result = updateContactUseCase(contact)

                if (result.isSuccess) {
                    _state.update {
                        it.copy(
                            isSaving = false,
                            isContactSaved = true
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            isSaving = false,
                            error = result.exceptionOrNull()?.message ?: "Failed to update contact"
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isSaving = false,
                        error = e.message ?: "An error occurred"
                    )
                }
            }
        }
    }

    private fun validateFirstName(name: String): String? {
        return when {
            name.isBlank() -> "First name is required"
            name.length < 2 -> "First name must be at least 2 characters"
            else -> null
        }
    }

    private fun validateLastName(name: String): String? {
        return when {
            name.isBlank() -> "Last name is required"
            name.length < 2 -> "Last name must be at least 2 characters"
            else -> null
        }
    }

    private fun validatePhoneNumber(number: String): String? {
        return when {
            number.isBlank() -> "Phone number is required"
            number.length < 10 -> "Phone number must be at least 10 digits"
            else -> null
        }
    }

    private fun formatPhoneNumber(number: String): String {
        return number.filter { it.isDigit() || it == '+' }
    }

    private fun uploadImage(imageBytes: ByteArray) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                val result = uploadImageUseCase(imageBytes)

                if (result.isSuccess) {
                    val imageUrl = result.getOrNull()
                    _state.update {
                        it.copy(
                            isLoading = false,
                            profileImageUrl = imageUrl,
                            error = null
                        )
                    }

                    if (editingContactId != null && imageUrl != null) {
                        updateContactImageUrl(imageUrl)
                    }
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.exceptionOrNull()?.message ?: "Failed to upload image"
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to upload image"
                    )
                }
            }
        }
    }

    private fun updateContactImageUrl(imageUrl: String) {
        editingContactId?.let { id ->
            viewModelScope.launch {
                try {
                    val contact = com.example.phonecontact.domain.model.Contact(
                        id = id,
                        firstName = _state.value.firstName,
                        lastName = _state.value.lastName,
                        phoneNumber = _state.value.phoneNumber,
                        profileImageUrl = imageUrl
                    )
                    updateContactUseCase(contact)
                } catch (e: Exception) {
                }
            }
        }
    }
}