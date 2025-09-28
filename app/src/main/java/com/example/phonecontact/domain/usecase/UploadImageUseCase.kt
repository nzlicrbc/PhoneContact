package com.example.phonecontact.domain.usecase

import com.example.phonecontact.domain.repository.ContactRepository
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(
    private val repository: ContactRepository
) {
    suspend operator fun invoke(imageBytes: ByteArray): Result<String> {
        return repository.uploadImage(imageBytes)
    }
}