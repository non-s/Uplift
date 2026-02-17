package com.motivacional.frases.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.motivacional.frases.data.model.Quote
import com.motivacional.frases.data.repository.QuoteRepository
import com.motivacional.frases.utils.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class QuoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = QuoteRepository(application)
    private val preferencesManager = PreferencesManager(application)

    private val _quote = MutableStateFlow<Quote?>(null)
    val quote: StateFlow<Quote?> = _quote

    private val _quotes = MutableStateFlow<List<Quote>>(emptyList())
    val quotes: StateFlow<List<Quote>> = _quotes

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadAllQuotes()
    }

    private fun loadAllQuotes() {
        viewModelScope.launch {
            try {
                val allQuotes = repository.getAllQuotes()
                _quotes.value = allQuotes
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    /**
     * Carrega a frase do dia. Só busca uma nova frase se for um novo dia.
     */
    fun loadDailyQuote() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currentTime = System.currentTimeMillis()
                val lastQuoteTime = preferencesManager.getLastQuoteTimestamp()
                val lastQuoteId = preferencesManager.getLastQuoteId()

                // Verifica se é um novo dia (compara apenas a data, não o horário)
                val shouldLoadNewQuote = !isSameDay(currentTime, lastQuoteTime)

                if (shouldLoadNewQuote || lastQuoteId.isEmpty()) {
                    // Carregar nova frase aleatória
                    val randomQuote = repository.getRandomQuote()
                    if (randomQuote != null) {
                        _quote.value = randomQuote
                        // Salvar timestamp e ID da nova frase
                        preferencesManager.saveLastQuoteTimestamp(currentTime)
                        preferencesManager.saveLastQuoteId(randomQuote.id)
                    }
                } else {
                    // Carregar a frase do dia atual (mesma de antes)
                    val allQuotes = repository.getAllQuotes()
                    val todayQuote = allQuotes.find { it.id == lastQuoteId }
                    _quote.value = todayQuote ?: repository.getRandomQuote()
                }
            } catch (e: Exception) {
                // Handle error
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Verifica se duas timestamps são do mesmo dia
     */
    private fun isSameDay(time1: Long, time2: Long): Boolean {
        if (time2 == 0L) return false
        val cal1 = Calendar.getInstance().apply { timeInMillis = time1 }
        val cal2 = Calendar.getInstance().apply { timeInMillis = time2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    fun loadRandomQuote() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val randomQuote = repository.getRandomQuote()
                _quote.value = randomQuote
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite() {
        val currentQuote = _quote.value
        if (currentQuote != null) {
            viewModelScope.launch {
                val updatedQuote = currentQuote.copy(isFavorite = !currentQuote.isFavorite)
                repository.toggleFavorite(updatedQuote)
                _quote.value = updatedQuote
            }
        }
    }

    fun toggleFavorite(quote: Quote) {
        viewModelScope.launch {
            val updatedQuote = quote.copy(isFavorite = !quote.isFavorite)
            repository.toggleFavorite(updatedQuote)
            // Atualizar a lista de frases se necessário
            val updatedList = _quotes.value.map {
                if (it.id == updatedQuote.id) updatedQuote else it
            }
            _quotes.value = updatedList
        }
    }
}
