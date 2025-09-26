package com.example.phonecontact.domain.usecase

import com.example.phonecontact.domain.model.Contact
import com.example.phonecontact.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(
    private val repository: ContactRepository
) {
    operator fun invoke(): Flow<List<Contact>> {
        return repository.getAllContacts()
    }
}