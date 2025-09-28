package com.example.phonecontact.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: T? = null,

    @SerializedName("status")
    val status: Int? = null
)

data class ImageData(
    @SerializedName("imageUrl")
    val imageUrl: String
)