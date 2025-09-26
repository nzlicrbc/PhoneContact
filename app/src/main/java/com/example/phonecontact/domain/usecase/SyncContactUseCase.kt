package com.example.phonecontact.domain.usecase

import com.example.phonecontact.domain.model.Contact
import com.example.phonecontact.domain.repository.ContactRepository
import javax.inject.Inject

class SyncContactsUseCase @Inject constructor(
    private val repository: ContactRepository
) {
    suspend operator fun invoke(): Result<List<Contact>> {
        return repository.syncAllContacts()
    }
}