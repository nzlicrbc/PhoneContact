package com.example.phonecontact.domain.usecase

import com.example.phonecontact.domain.repository.ContactRepository
import javax.inject.Inject

class DeleteContactUseCase @Inject constructor(
    private val repository: ContactRepository
) {
    suspend operator fun invoke(contactId: String) {
        repository.deleteContact(contactId)
    }
}