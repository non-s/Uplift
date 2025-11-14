package com.motivacional.frases.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.motivacional.frases.data.model.Quote
import com.motivacional.frases.data.repository.QuoteRepository
import com.motivacional.frases.utils.FavoritesManager
import kotlinx.coroutines.launch

class QuoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = QuoteRepository()
    private val favoritesManager = FavoritesManager(application)
    
    private val _quotes = MutableLiveData<List<Quote>>()
    val quotes: LiveData<List<Quote>> = _quotes
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _currentCategory = MutableLiveData<String>("all")
    val currentCategory: LiveData<String> = _currentCategory
    
    init {
        loadQuotes()
    }
    
    fun loadQuotes(category: String = "all") {
        _currentCategory.value = category
        _isLoading.value = true
        
        viewModelScope.launch {
            try {
                val quotesList = repository.getQuotes(
                    if (category == "all") null else category
                )
                
                // Marcar favoritos
                val favoriteIds = favoritesManager.getFavoriteIds()
                quotesList.forEach { quote ->
                    quote.isFavorite = favoriteIds.contains(quote.id)
                }
                
                _quotes.value = quotesList
            } catch (e: Exception) {
                _quotes.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun toggleFavorite(quote: Quote) {
        if (quote.isFavorite) {
            favoritesManager.removeFavorite(quote.id)
            quote.isFavorite = false
        } else {
            favoritesManager.addFavorite(quote.id)
            quote.isFavorite = true
        }
        
        // Atualizar lista
        _quotes.value = _quotes.value
    }
    
    fun loadFavorites() {
        _isLoading.value = true
        
        viewModelScope.launch {
            try {
                val allQuotes = repository.getQuotes()
                val favoriteIds = favoritesManager.getFavoriteIds()
                
                val favoriteQuotes = allQuotes.filter { quote ->
                    favoriteIds.contains(quote.id)
                }.map { it.apply { isFavorite = true } }
                
                _quotes.value = favoriteQuotes
            } catch (e: Exception) {
                _quotes.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun initializeDatabase() {
        viewModelScope.launch {
            repository.addInitialQuotes()
            loadQuotes()
        }
    }
}
