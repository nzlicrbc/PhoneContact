package com.example.phonecontact.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "search_query")
    val searchQuery: String,

    @ColumnInfo(name = "searched_at")
    val searchedAt: Long = System.currentTimeMillis()
)