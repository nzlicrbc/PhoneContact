package com.example.phonecontact.data.datasource.remote

import com.example.phonecontact.data.remote.api.ApiService
import com.example.phonecontact.data.remote.dto.ContactDto
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun createContact(contact: ContactDto): ContactDto {
        val response = apiService.createContact(contact)
        if (response.success && response.data != null) {
            return response.data
        } else {
            throw Exception(response.message ?: "Failed to create contact")
        }
    }

    suspend fun getContactById(id: String): ContactDto {
        val response = apiService.getContactById(id)
        if (response.success && response.data != null) {
            return response.data
        } else {
            throw Exception(response.message ?: "Failed to get contact")
        }
    }

    suspend fun updateContact(id: String, contact: ContactDto): ContactDto {
        val response = apiService.updateContact(id, contact)
        if (response.success && response.data != null) {
            return response.data
        } else {
            throw Exception(response.message ?: "Failed to update contact")
        }
    }

    suspend fun deleteContact(id: String) {
        val response = apiService.deleteContact(id)
        if (!response.success) {
            throw Exception(response.message ?: "Failed to delete contact")
        }
    }

    suspend fun getAllContacts(): List<ContactDto> {
        val response = apiService.getAllContacts()
        if (response.success && response.data != null) {
            return response.data
        } else {
            throw Exception(response.message ?: "Failed to get all contacts")
        }
    }

    suspend fun uploadImage(imageByteArray: ByteArray, fileName: String): String {
        val requestFile = imageByteArray.toRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", fileName, requestFile)

        val response = apiService.uploadImage(imagePart)
        if (response.success && response.data != null) {
            return response.data
        } else {
            throw Exception(response.message ?: "Failed to upload image")
        }
    }
}