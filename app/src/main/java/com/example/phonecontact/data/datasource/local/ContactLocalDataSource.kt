package com.example.phonecontact.data.datasource.local

import com.example.phonecontact.data.local.dao.ContactDao
import com.example.phonecontact.data.local.dao.SearchHistoryDao
import com.example.phonecontact.data.local.entity.ContactEntity
import com.example.phonecontact.data.local.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactLocalDataSource @Inject constructor(
    private val contactDao: ContactDao,
    private val searchHistoryDao: SearchHistoryDao
) {

    fun getAllContacts(): Flow<List<ContactEntity>> {
        return contactDao.getAllContacts()
    }

    suspend fun getContactById(id: String): ContactEntity? {
        return contactDao.getContactById(id)
    }

    fun searchContacts(query: String): Flow<List<ContactEntity>> {
        return contactDao.searchContacts(query)
    }

    suspend fun insertContact(contact: ContactEntity): Long {
        return contactDao.insertContact(contact)
    }

    suspend fun updateContact(contact: ContactEntity): Int {
        return contactDao.updateContact(contact)
    }

    suspend fun deleteContact(contact: ContactEntity): Int {
        return contactDao.deleteContact(contact)
    }

    fun getRecentSearches(limit: Int = 10): Flow<List<SearchHistoryEntity>> {
        return searchHistoryDao.getRecentSearches(limit)
    }

    suspend fun insertSearch(searchHistory: SearchHistoryEntity): Long {
        return searchHistoryDao.insertSearch(searchHistory)
    }

    suspend fun clearAllSearchHistory() {
        return searchHistoryDao.clearAllHistory()
    }
}