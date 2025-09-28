package com.example.phonecontact.presentation.profile

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.provider.ContactsContract
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonecontact.domain.repository.ContactRepository
import com.example.phonecontact.domain.usecase.DeleteContactUseCase
import com.example.phonecontact.presentation.navigation.NavigationArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    private val deleteContactUseCase: DeleteContactUseCase,
    @ApplicationContext private val context: Context
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
        val contact = _state.value.contact ?: return

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        viewModelScope.launch {
            try {
                val contentValues = ContentValues().apply {
                    put(ContactsContract.RawContacts.ACCOUNT_TYPE, null as String?)
                    put(ContactsContract.RawContacts.ACCOUNT_NAME, null as String?)
                }

                val rawContactUri = context.contentResolver.insert(
                    ContactsContract.RawContacts.CONTENT_URI, contentValues
                )
                val rawContactId = rawContactUri?.lastPathSegment?.toLong() ?: return@launch

                val nameValues = ContentValues().apply {
                    put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
                    put(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, contact.firstName)
                    put(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, contact.lastName)
                }
                context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, nameValues)

                val phoneValues = ContentValues().apply {
                    put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
                    put(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    put(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.phoneNumber)
                    put(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                }
                context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, phoneValues)

                _state.update { it.copy(isSavedToDevice = true) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }
}