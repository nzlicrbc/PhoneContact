package com.example.phonecontact.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "first_name")
    val firstName: String,

    @ColumnInfo(name = "last_name")
    val lastName: String,

    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,

    @ColumnInfo(name = "profile_image_url")
    val profileImageUrl: String? = null,

    @ColumnInfo(name = "is_in_device_contacts")
    val isInDeviceContacts: Boolean = false,

    @ColumnInfo(name = "created_at")
    val createdAt: String? = null

)