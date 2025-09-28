package com.example.phonecontact.data.datasource.remote

import android.content.Context
import com.example.phonecontact.R
import com.example.phonecontact.data.remote.api.ApiService
import com.example.phonecontact.data.remote.dto.ContactDto
import com.example.phonecontact.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    @ApplicationContext private val context: Context
) {

    suspend fun createContact(contact: ContactDto): ContactDto {
        val response = apiService.createContact(contact)
        if (response.success && response.data != null) {
            return response.data
        } else {
            throw Exception(context.getString(R.string.error_create_contact))
        }
    }

    suspend fun getContactById(id: String): ContactDto {
        val response = apiService.getContactById(id)
        if (response.success && response.data != null) {
            return response.data
        } else {
            throw Exception(context.getString(R.string.error_get_contact))
        }
    }

    suspend fun updateContact(id: String, contact: ContactDto): ContactDto {
        val response = apiService.updateContact(id, contact)
        if (response.success && response.data != null) {
            return response.data
        } else {
            throw Exception(context.getString(R.string.error_update_contact))
        }
    }

    suspend fun deleteContact(id: String) {
        val response = apiService.deleteContact(id)
        if (!response.success) {
            throw Exception(context.getString(R.string.error_delete_contact))
        }
    }

    suspend fun getAllContacts(): List<ContactDto> {
        val response = apiService.getAllContacts()
        if (response.success && response.data != null) {
            return response.data
        } else {
            throw Exception(context.getString(R.string.error_get_all_contacts))
        }
    }

    suspend fun uploadImage(imageByteArray: ByteArray, fileName: String): String {
        val requestFile = imageByteArray.toRequestBody(Constants.IMAGE_MEDIA_TYPE.toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData(Constants.IMAGE_PART_NAME, fileName, requestFile)

        val response = apiService.uploadImage(imagePart)
        if (response.success && response.data != null) {
            return response.data.imageUrl
        } else {
            throw Exception(context.getString(R.string.error_upload_image))
        }
    }
}