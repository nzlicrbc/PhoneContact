package com.example.phonecontact.data.mapper

import com.example.phonecontact.data.local.entity.ContactEntity
import com.example.phonecontact.data.local.entity.SearchHistoryEntity
import com.example.phonecontact.data.remote.dto.ContactDto
import com.example.phonecontact.domain.model.Contact
import com.example.phonecontact.domain.model.SearchHistory
import java.util.UUID

fun ContactEntity.toDomainModel(): Contact {
    return Contact(
        id = id,
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber,
        profileImageUrl = profileImageUrl,
        isInDeviceContacts = isInDeviceContacts,
        createdAt = createdAt
    )
}

fun Contact.toEntity(): ContactEntity {
    return ContactEntity(
        id = id.ifEmpty { UUID.randomUUID().toString() },
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber,
        profileImageUrl = profileImageUrl,
        isInDeviceContacts = isInDeviceContacts,
        createdAt = createdAt
    )
}

fun ContactDto.toDomainModel(): Contact {
    return Contact(
        id = id ?: UUID.randomUUID().toString(),
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber,
        profileImageUrl = profileImageUrl,
        isInDeviceContacts = false,
        createdAt = createdAt
    )
}

fun Contact.toDto(): ContactDto {
    return ContactDto(
        id = if (id.isNotEmpty()) id else null,
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber,
        profileImageUrl = profileImageUrl,
        createdAt = createdAt
    )
}

fun ContactDto.toEntity(): ContactEntity {
    return ContactEntity(
        id = id ?: UUID.randomUUID().toString(),
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber,
        profileImageUrl = profileImageUrl,
        isInDeviceContacts = false,
        createdAt = createdAt
    )
}

fun SearchHistoryEntity.toDomainModel(): SearchHistory {
    return SearchHistory(
        id = id,
        searchQuery = searchQuery,
        searchedAt = searchedAt
    )
}

fun SearchHistory.toEntity(): SearchHistoryEntity {
    return SearchHistoryEntity(
        id = id,
        searchQuery = searchQuery,
        searchedAt = searchedAt
    )
}

fun List<ContactEntity>.toDomainModelList(): List<Contact> {
    return this.map { it.toDomainModel() }
}

fun List<Contact>.toEntityList(): List<ContactEntity> {
    return this.map { it.toEntity() }
}

fun List<ContactDto>.toDomainModelListFromDto(): List<Contact> {
    return this.map { it.toDomainModel() }
}

fun List<SearchHistoryEntity>.toSearchHistoryDomainList(): List<SearchHistory> {
    return this.map { it.toDomainModel() }
}