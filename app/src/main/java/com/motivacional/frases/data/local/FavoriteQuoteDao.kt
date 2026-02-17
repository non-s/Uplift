package com.motivacional.frases.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteQuoteDao {
    @Query("SELECT * FROM favorite_quotes ORDER BY favoritedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteQuoteEntity>>

    @Query("SELECT * FROM favorite_quotes WHERE id = :quoteId")
    suspend fun getFavoriteById(quoteId: String): FavoriteQuoteEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_quotes WHERE id = :quoteId)")
    suspend fun isFavorite(quoteId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteQuoteEntity)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteQuoteEntity)

    @Query("DELETE FROM favorite_quotes WHERE id = :quoteId")
    suspend fun deleteFavoriteById(quoteId: String)

    @Query("DELETE FROM favorite_quotes")
    suspend fun deleteAllFavorites()
}
