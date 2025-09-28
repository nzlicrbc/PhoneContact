package com.example.phonecontact.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.phonecontact.data.local.entity.ContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Query("SELECT * FROM contacts ORDER BY first_name ASC, last_name ASC")
    fun getAllContacts(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts WHERE id = :id LIMIT 1")
    suspend fun getContactById(id: String): ContactEntity?

    @Query("""
        SELECT * FROM contacts 
        WHERE first_name LIKE '%' || :query || '%' 
        OR last_name LIKE '%' || :query || '%'
        OR (first_name || ' ' || last_name) LIKE '%' || :query || '%'
        ORDER BY first_name ASC, last_name ASC
    """)
    fun searchContacts(query: String): Flow<List<ContactEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: ContactEntity): Long

    @Update
    suspend fun updateContact(contact: ContactEntity): Int

    @Delete
    suspend fun deleteContact(contact: ContactEntity): Int

    @Query("UPDATE contacts SET is_in_device_contacts = :isInDeviceContacts WHERE id = :contactId")
    suspend fun updateDeviceStatus(contactId: String, isInDeviceContacts: Boolean)
}