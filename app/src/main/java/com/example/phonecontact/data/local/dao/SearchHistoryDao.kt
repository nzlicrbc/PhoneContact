package com.example.phonecontact.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.phonecontact.data.local.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {

    @Query("SELECT * FROM search_history ORDER BY searched_at DESC LIMIT :limit")
    fun getRecentSearches(limit: Int = 10): Flow<List<SearchHistoryEntity>>

    @Insert
    suspend fun insertSearch(searchHistory: SearchHistoryEntity): Long

    @Query("DELETE FROM search_history")
    suspend fun clearAllHistory()
}