package com.example.phonecontact.presentation.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonecontact.domain.repository.ContactRepository
import com.example.phonecontact.domain.usecase.DeleteContactUseCase
import com.example.phonecontact.presentation.navigation.NavigationArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ContactRepository,
    private val deleteContactUseCase: DeleteContactUseCase
) : ViewModel() {

    private val contactId: String = checkNotNull(savedStateHandle[NavigationArgs.CONTACT_ID])

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        loadContact()
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.LoadContact -> loadContact()
            ProfileEvent.ShowMenu -> {
                _state.update { it.copy(showMenu = true) }
            }
            ProfileEvent.HideMenu -> {
                _state.update { it.copy(showMenu = false) }
            }
            ProfileEvent.DeleteClicked -> {
                _state.update { it.copy(showDeleteDialog = true, showMenu = false) }
            }
            ProfileEvent.ConfirmDelete -> {
                deleteContact()
            }
            ProfileEvent.CancelDelete -> {
                _state.update { it.copy(showDeleteDialog = false) }
            }
            ProfileEvent.SaveToDevice -> {
                saveToDevice()
            }
            else -> {}
        }
    }

    private fun loadContact() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val contact = repository.getContactById(contactId)
                _state.update {
                    it.copy(
                        contact = contact,
                        isLoading = false,
                        isSavedToDevice = contact?.isInDeviceContacts ?: false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = e.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun deleteContact() {
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true, showDeleteDialog = false) }

            try {
                deleteContactUseCase(contactId)
                _state.update {
                    it.copy(
                        isDeleting = false,
                        isDeleteSuccess = true
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isDeleting = false,
                        error = e.message
                    )
                }
            }
        }
    }

    private fun saveToDevice() {
    }
}