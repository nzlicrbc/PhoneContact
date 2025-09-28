package com.example.phonecontact.domain.usecase

import com.example.phonecontact.domain.model.Contact
import com.example.phonecontact.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchContactUseCase @Inject constructor(
    private val repository: ContactRepository
) {
    operator fun invoke(query: String): Flow<List<Contact>> {
        return if (query.isBlank()) {
            repository.getAllContacts()
        } else {
            repository.searchContacts(query.trim())
        }
    }
}