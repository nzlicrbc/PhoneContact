package com.example.phonecontact.domain.usecase

import com.example.phonecontact.domain.model.Contact
import com.example.phonecontact.domain.repository.ContactRepository
import javax.inject.Inject

class UpdateContactUseCase @Inject constructor(
    private val repository: ContactRepository
) {
    suspend operator fun invoke(contact: Contact): Result<Contact> {
        return try {
            val updatedContact = contact.copy(
                firstName = contact.firstName.trim(),
                lastName = contact.lastName.trim(),
                phoneNumber = contact.phoneNumber.trim()
            )

            val result = repository.updateContactRemote(updatedContact)
            if (result.isSuccess) {
                Result.success(result.getOrThrow())
            } else {
                repository.updateContact(updatedContact)
                Result.success(updatedContact)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}