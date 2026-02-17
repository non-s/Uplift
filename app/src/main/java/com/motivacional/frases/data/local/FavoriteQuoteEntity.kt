package com.motivacional.frases.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_quotes")
data class FavoriteQuoteEntity(
    @PrimaryKey
    val id: String,
    val text: String,
    val author: String,
    val category: String,
    val timestamp: Long,
    val favoritedAt: Long = System.currentTimeMillis()
)
