package com.example.phonecontact.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.phonecontact.data.local.dao.ContactDao
import com.example.phonecontact.data.local.dao.SearchHistoryDao
import com.example.phonecontact.data.local.entity.ContactEntity
import com.example.phonecontact.data.local.entity.SearchHistoryEntity
import com.example.phonecontact.utils.Constants

@Database(
    entities = [
        ContactEntity::class,
        SearchHistoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDao
    abstract fun searchHistoryDao(): SearchHistoryDao

    companion object {
        const val DATABASE_NAME = Constants.DATABASE_NAME
    }
}