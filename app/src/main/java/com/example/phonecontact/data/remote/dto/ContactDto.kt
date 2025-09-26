package com.example.phonecontact.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ContactDto(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("firstName")
    val firstName: String,

    @SerializedName("lastName")
    val lastName: String,

    @SerializedName("phoneNumber")
    val phoneNumber: String,

    @SerializedName("profileImageUrl")
    val profileImageUrl: String? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("status")
    val status: Int? = null
)
