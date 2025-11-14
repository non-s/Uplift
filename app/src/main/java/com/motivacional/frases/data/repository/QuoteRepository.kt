package com.motivacional.frases.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.motivacional.frases.data.model.Quote
import kotlinx.coroutines.tasks.await

class QuoteRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val quotesCollection = firestore.collection("quotes")
    
    suspend fun getQuotes(category: String? = null): List<Quote> {
        return try {
            val query = if (category != null && category != "all") {
                quotesCollection
                    .whereEqualTo("category", category)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
            } else {
                quotesCollection.orderBy("timestamp", Query.Direction.DESCENDING)
            }
            
            query.get().await().toObjects(Quote::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun getRandomQuote(): Quote? {
        return try {
            val quotes = getQuotes()
            quotes.randomOrNull()
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun addQuote(quote: Quote): Boolean {
        return try {
            quotesCollection.add(quote).await()
            true
        } catch (e: Exception) {
            false
        }
    }
    
    // Método para popular database com frases iniciais
    suspend fun addInitialQuotes() {
        val initialQuotes = listOf(
            Quote(text = "O sucesso é a soma de pequenos esforços repetidos dia após dia.", author = "Robert Collier", category = "success"),
            Quote(text = "Acredite em si mesmo e todo o resto se encaixará.", author = "Unknown", category = "motivation"),
            Quote(text = "A persistência é o caminho do êxito.", author = "Charles Chaplin", category = "success"),
            Quote(text = "Você é mais forte do que pensa.", author = "Unknown", category = "motivation"),
            Quote(text = "O amor é a única força capaz de transformar um inimigo em amigo.", author = "Martin Luther King", category = "love"),
            Quote(text = "A vida é 10% o que acontece com você e 90% como você reage a isso.", author = "Charles Swindoll", category = "life"),
            Quote(text = "Seja a mudança que você deseja ver no mundo.", author = "Mahatma Gandhi", category = "wisdom"),
            Quote(text = "O único modo de fazer um ótimo trabalho é amar o que você faz.", author = "Steve Jobs", category = "success"),
            Quote(text = "Não conte os dias, faça os dias contarem.", author = "Muhammad Ali", category = "motivation"),
            Quote(text = "Grandes coisas nunca vêm de zonas de conforto.", author = "Unknown", category = "motivation")
        )
        
        initialQuotes.forEach { quote ->
            addQuote(quote)
        }
    }
}
