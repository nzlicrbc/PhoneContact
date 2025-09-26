package com.example.phonecontact.domain.usecase

import com.example.phonecontact.domain.repository.ContactRepository
import javax.inject.Inject

class DeleteContactUseCase @Inject constructor(
    private val repository: ContactRepository
) {
    suspend operator fun invoke(contact: String): Result<Unit> {
        return try {
            val result = repository.deleteContactRemote(contact.id)
            if (result.isSuccess) {
                Result.success(Unit)
            } else {
                repository.deleteContact(contact)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}