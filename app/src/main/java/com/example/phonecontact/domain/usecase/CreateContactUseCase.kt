package com.example.phonecontact.domain.usecase

import com.example.phonecontact.domain.model.Contact
import com.example.phonecontact.domain.repository.ContactRepository
import java.util.UUID
import javax.inject.Inject

class CreateContactUseCase @Inject constructor(
    private val repository: ContactRepository
) {
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        profileImageUrl: String? = null
    ): Result<Contact> {
        return try {
            val contact = Contact(
                id = UUID.randomUUID().toString(),
                firstName = firstName.trim(),
                lastName = lastName.trim(),
                phoneNumber = phoneNumber.trim(),
                profileImageUrl = profileImageUrl,
                createdAt = System.currentTimeMillis().toString()
            )

            val result = repository.createContactRemote(contact)
            if (result.isSuccess) {
                Result.success(result.getOrThrow())
            } else {
                repository.insertContact(contact)
                Result.success(contact)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}