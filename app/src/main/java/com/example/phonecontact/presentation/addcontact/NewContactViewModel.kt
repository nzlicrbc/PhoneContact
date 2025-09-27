package com.example.phonecontact.presentation.addcontact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonecontact.domain.usecase.CreateContactUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewContactViewModel @Inject constructor(
    private val createContactUseCase: CreateContactUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NewContactState())
    val state: StateFlow<NewContactState> = _state.asStateFlow()

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
                _state.update { it.copy(profileImageUri = event.uri) }
            }
            NewContactEvent.SaveContact -> {
                saveContact()
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

            val result = createContactUseCase(
                firstName = _state.value.firstName,
                lastName = _state.value.lastName,
                phoneNumber = _state.value.phoneNumber,
                profileImageUrl = _state.value.profileImageUrl
            )

            _state.update {
                if (result.isSuccess) {
                    it.copy(
                        isSaving = false,
                        isContactSaved = true
                    )
                } else {
                    it.copy(
                        isSaving = false,
                        error = result.exceptionOrNull()?.message ?: "Failed to save contact"
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
}