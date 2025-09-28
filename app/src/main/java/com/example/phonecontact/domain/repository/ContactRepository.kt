package com.example.phonecontact.domain.repository

import com.example.phonecontact.domain.model.Contact
import com.example.phonecontact.domain.model.SearchHistory
import kotlinx.coroutines.flow.Flow

interface ContactRepository {

    fun getAllContacts(): Flow<List<Contact>>
    suspend fun getContactById(id: String): Contact?
    fun searchContacts(query: String): Flow<List<Contact>>
    suspend fun insertContact(contact: Contact): Long
    suspend fun updateContact(contact: Contact): Int
    suspend fun deleteContact(contact: String): Int
    suspend fun syncAllContacts(): Result<List<Contact>>
    suspend fun createContactRemote(contact: Contact): Result<Contact>
    suspend fun updateContactRemote(contact: Contact): Result<Contact>
    suspend fun deleteContactRemote(contactId: String): Result<Unit>
    suspend fun uploadImage(imageByteArray: ByteArray): Result<String>
    fun getRecentSearches(limit: Int = 10): Flow<List<SearchHistory>>
    suspend fun insertSearch(searchHistory: SearchHistory): Long
    suspend fun clearAllSearchHistory()
    suspend fun updateContactDeviceStatus(contactId: String, isInDeviceContacts: Boolean)
}