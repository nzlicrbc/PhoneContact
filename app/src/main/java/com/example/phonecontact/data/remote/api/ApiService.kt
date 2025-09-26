package com.example.phonecontact.data.remote.api

import com.example.phonecontact.data.remote.dto.ApiResponse
import com.example.phonecontact.data.remote.dto.ContactDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @POST("api/User")
    suspend fun createContact(
        @Body contact: ContactDto
    ): ApiResponse<ContactDto>

    @GET("api/User/{id}")
    suspend fun getContactById(
        @Path("id") id: String
    ): ApiResponse<ContactDto>

    @PUT("api/User/{id}")
    suspend fun updateContact(
        @Path("id") id: String,
        @Body contact: ContactDto
    ): ApiResponse<ContactDto>

    @DELETE("api/User/{id}")
    suspend fun deleteContact(
        @Path("id") id: String
    ): ApiResponse<Any>

    @GET("api/User/GetAll")
    suspend fun getAllContacts(): ApiResponse<List<ContactDto>>

    @Multipart
    @POST("api/User/UploadImage")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): ApiResponse<String>
}