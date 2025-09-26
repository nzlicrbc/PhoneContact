package com.example.phonecontact.data.repository

import com.example.phonecontact.data.datasource.local.ContactLocalDataSource
import com.example.phonecontact.data.datasource.remote.ContactRemoteDataSource
import com.example.phonecontact.data.mapper.toDomainModel
import com.example.phonecontact.data.mapper.toDomainModelList
import com.example.phonecontact.data.mapper.toDomainModelListFromDto
import com.example.phonecontact.data.mapper.toDto
import com.example.phonecontact.data.mapper.toEntity
import com.example.phonecontact.data.mapper.toEntityList
import com.example.phonecontact.data.mapper.toSearchHistoryDomainList
import com.example.phonecontact.domain.model.Contact
import com.example.phonecontact.domain.model.SearchHistory
import com.example.phonecontact.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepositoryImpl @Inject constructor(
    private val localDataSource: ContactLocalDataSource,
    private val remoteDataSource: ContactRemoteDataSource
) : ContactRepository {

    override fun getAllContacts(): Flow<List<Contact>> {
        return localDataSource.getAllContacts().map { entities ->
            entities.toDomainModelList()
        }
    }

    override suspend fun getContactById(id: String): Contact? {
        return localDataSource.getContactById(id)?.toDomainModel()
    }

    override fun searchContacts(query: String): Flow<List<Contact>> {
        return localDataSource.searchContacts(query).map { entities ->
            entities.toDomainModelList()
        }
    }

    override suspend fun insertContact(contact: Contact): Long {
        return localDataSource.insertContact(contact.toEntity())
    }

    override suspend fun updateContact(contact: Contact): Int {
        return localDataSource.updateContact(contact.toEntity())
    }

    override suspend fun deleteContact(contact: Contact): Int {
        return localDataSource.deleteContact(contact.toEntity())
    }

    override suspend fun syncAllContacts(): Result<List<Contact>> {
        return try {
            val remoteContacts = remoteDataSource.getAllContacts()
            val domainContacts = remoteContacts.toDomainModelListFromDto()

            val entities = domainContacts.toEntityList()
            entities.forEach { entity ->
                localDataSource.insertContact(entity)
            }

            Result.success(domainContacts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createContactRemote(contact: Contact): Result<Contact> {
        return try {
            val remoteContact = remoteDataSource.createContact(contact.toDto())
            val domainContact = remoteContact.toDomainModel()

            localDataSource.insertContact(domainContact.toEntity())

            Result.success(domainContact)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateContactRemote(contact: Contact): Result<Contact> {
        return try {
            val remoteContact = remoteDataSource.updateContact(contact.id, contact.toDto())
            val domainContact = remoteContact.toDomainModel()

            localDataSource.updateContact(domainContact.toEntity())

            Result.success(domainContact)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteContactRemote(contactId: String): Result<Unit> {
        return try {
            remoteDataSource.deleteContact(contactId)

            val localContact = localDataSource.getContactById(contactId)
            localContact?.let {
                localDataSource.deleteContact(it)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadImage(imageByteArray: ByteArray): Result<String> {
        return try {
            val fileName = "image_${System.currentTimeMillis()}.jpg"
            val imageUrl = remoteDataSource.uploadImage(imageByteArray, fileName)
            Result.success(imageUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getRecentSearches(limit: Int): Flow<List<SearchHistory>> {
        return localDataSource.getRecentSearches(limit).map { entities ->
            entities.toSearchHistoryDomainList()
        }
    }

    override suspend fun insertSearch(searchHistory: SearchHistory): Long {
        return localDataSource.insertSearch(searchHistory.toEntity())
    }

    override suspend fun clearAllSearchHistory() {
        return localDataSource.clearAllSearchHistory()
    }
}