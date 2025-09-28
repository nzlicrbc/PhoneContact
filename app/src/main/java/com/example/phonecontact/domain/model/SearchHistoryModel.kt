package com.example.phonecontact.domain.model

data class SearchHistory(
    val id: Long = 0,
    val searchQuery: String,
    val searchedAt: Long = System.currentTimeMillis()
)