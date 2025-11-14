package com.motivacional.frases.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoritesManager(context: Context) {
    private val prefs: SharedPreferences = 
        context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    
    fun getFavoriteIds(): Set<String> {
        val json = prefs.getString("favorites", "[]") ?: "[]"
        val type = object : TypeToken<Set<String>>() {}.type
        return gson.fromJson(json, type) ?: emptySet()
    }
    
    fun addFavorite(quoteId: String) {
        val favorites = getFavoriteIds().toMutableSet()
        favorites.add(quoteId)
        saveFavorites(favorites)
    }
    
    fun removeFavorite(quoteId: String) {
        val favorites = getFavoriteIds().toMutableSet()
        favorites.remove(quoteId)
        saveFavorites(favorites)
    }
    
    fun isFavorite(quoteId: String): Boolean {
        return getFavoriteIds().contains(quoteId)
    }
    
    private fun saveFavorites(favorites: Set<String>) {
        val json = gson.toJson(favorites)
        prefs.edit().putString("favorites", json).apply()
    }
}
