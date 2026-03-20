package com.motivacional.frases.data.repository

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.motivacional.frases.data.QuotesData
import com.motivacional.frases.data.local.*
import com.motivacional.frases.data.model.Quote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class QuoteRepository(private val context: Context) {
    private val database = QuoteDatabase.getDatabase(context)
    private val quoteDao: QuoteDao = database.quoteDao()
    private val favoriteDao: FavoriteQuoteDao = database.favoriteQuoteDao()

    // Firebase Firestore
    private val firestore = FirebaseFirestore.getInstance()
    private val quotesCollection = firestore.collection("quotes")

    companion object {
        private const val TAG = "QuoteRepository"
    }

    // Inicializar banco com as 500+ frases na primeira execução
    suspend fun initializeIfNeeded() {
        val count = quoteDao.getQuoteCount()
        if (count == 0) {
            val initialQuotes = QuotesData.getInitialQuotes()
            val entities = initialQuotes.map { quote ->
                QuoteEntity(
                    text = quote.text,
                    author = quote.author,
                    category = quote.category,
                    timestamp = quote.timestamp
                )
            }
            quoteDao.insertQuotes(entities)
        }
    }

    suspend fun getQuotes(category: String? = null): Flow<List<Quote>> {
        initializeIfNeeded()

        val flow = if (category != null && category != "all") {
            quoteDao.getQuotesByCategory(category)
        } else {
            quoteDao.getAllQuotes()
        }

        return flow.map { quotes ->
            val favoriteIds = favoriteDao.getAllFavorites().firstOrNull()?.map { it.id } ?: emptyList()
            quotes.map { it.toQuote(isFavorite = favoriteIds.contains(it.id.toString())) }
        }
    }

    suspend fun getAllQuotes(): List<Quote> {
        return try {
            initializeIfNeeded()
            val quotes = quoteDao.getAllQuotes().firstOrNull() ?: emptyList()
            val favoriteIds = favoriteDao.getAllFavorites().firstOrNull()?.map { it.id } ?: emptyList()
            quotes.map { it.toQuote(isFavorite = favoriteIds.contains(it.id.toString())) }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getRandomQuote(): Quote? {
        return try {
            initializeIfNeeded()
            quoteDao.getRandomQuote()?.toQuote()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun addQuote(quote: Quote): Boolean {
        return try {
            val entity = QuoteEntity(
                text = quote.text,
                author = quote.author,
                category = quote.category,
                timestamp = quote.timestamp
            )
            quoteDao.insertQuote(entity)
            true
        } catch (e: Exception) {
            false
        }
    }

    // Método para popular database com 500+ frases (manual)
    suspend fun addInitialQuotes() {
        val initialQuotes = QuotesData.getInitialQuotes()
        val entities = initialQuotes.map { quote ->
            QuoteEntity(
                text = quote.text,
                author = quote.author,
                category = quote.category,
                timestamp = quote.timestamp
            )
        }
        quoteDao.insertQuotes(entities)
    }

    // Sincronizar frases locais com Firebase
    suspend fun syncLocalQuotesToFirebase(): Result<Int> {
        return try {
            Log.d(TAG, "Iniciando sincronização com Firebase...")

            // Buscar todas as frases locais
            val localQuotes = quoteDao.getAllQuotes().firstOrNull() ?: emptyList()

            Log.d(TAG, "Encontradas ${localQuotes.size} frases locais")

            if (localQuotes.isEmpty()) {
                Log.d(TAG, "Nenhuma frase local para sincronizar")
                return Result.success(0)
            }

            val batch = firestore.batch()
            var successCount = 0

            localQuotes.forEach { entity ->
                try {
                    val quote = Quote(
                        id = "",  // Firebase vai gerar um ID
                        text = entity.text,
                        author = entity.author,
                        category = entity.category,
                        timestamp = entity.timestamp,
                        isFavorite = false
                    )

                    // Adicionar ao lote
                    val newDocRef = quotesCollection.document() // Cria uma nova referência de documento com ID automático
                    batch.set(newDocRef, quote)
                    successCount++
                } catch (e: Exception) {
                    Log.e(TAG, "Erro ao adicionar frase ao lote: ${entity.text}", e)
                }
            }

            batch.commit().await() // Comita o lote
            Log.d(TAG, "Sincronização completa! $successCount frases enviadas ao Firebase via batch")
            Result.success(successCount)
        } catch (e: Exception) {
            Log.e(TAG, "Erro na sincronização com Firebase", e)
            Result.failure(e)
        }
    }

    // Baixar frases do Firebase e salvar localmente
    suspend fun syncFirebaseToLocal(): Result<Int> {
        return try {
            Log.d(TAG, "Baixando frases do Firebase...")

            val snapshot = quotesCollection.get().await()
            val firebaseQuotes = snapshot.toObjects(Quote::class.java)

            Log.d(TAG, "Encontradas ${firebaseQuotes.size} frases no Firebase")

            if (firebaseQuotes.isEmpty()) {
                return Result.success(0)
            }

            // Limpar banco local antes de sincronizar
            quoteDao.deleteAllQuotes()

            // Inserir frases do Firebase no banco local
            val entities = firebaseQuotes.map { quote ->
                QuoteEntity(
                    text = quote.text,
                    author = quote.author,
                    category = quote.category,
                    timestamp = quote.timestamp
                )
            }

            quoteDao.insertQuotes(entities)

            Log.d(TAG, "Sincronização do Firebase completa! ${entities.size} frases baixadas")
            Result.success(entities.size)
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao baixar frases do Firebase", e)
            Result.failure(e)
        }
    }

    // Sincronização bidirecional
    suspend fun fullSync(): Result<String> {
        return try {
            // Primeiro, enviar frases locais para Firebase
            val uploadResult = syncLocalQuotesToFirebase()
            val uploadedCount = uploadResult.getOrDefault(0)

            // Depois, baixar todas as frases do Firebase (incluindo as que acabamos de enviar)
            val downloadResult = syncFirebaseToLocal()
            val downloadedCount = downloadResult.getOrDefault(0)

            val message = "Sincronização completa! Enviadas: $uploadedCount, Baixadas: $downloadedCount"
            Log.d(TAG, message)
            Result.success(message)
        } catch (e: Exception) {
            Log.e(TAG, "Erro na sincronização completa", e)
            Result.failure(e)
        }
    }

    // Métodos de Favoritos
    fun getFavoriteQuotes(): Flow<List<Quote>> {
        return favoriteDao.getAllFavorites().map { favorites ->
            favorites.map { it.toQuote() }
        }
    }

    suspend fun toggleFavorite(quote: Quote) {
        if (favoriteDao.isFavorite(quote.id)) {
            favoriteDao.deleteFavoriteById(quote.id)
        } else {
            val favorite = FavoriteQuoteEntity(
                id = quote.id,
                text = quote.text,
                author = quote.author,
                category = quote.category,
                timestamp = quote.timestamp
            )
            favoriteDao.insertFavorite(favorite)
        }
    }

    suspend fun isFavorite(quoteId: String): Boolean {
        return favoriteDao.isFavorite(quoteId)
    }

    suspend fun removeFavorite(quoteId: String) {
        favoriteDao.deleteFavoriteById(quoteId)
    }
}

// Extension functions para converter Entities em Quote
private fun QuoteEntity.toQuote(isFavorite: Boolean = false): Quote {
    return Quote(
        id = this.id.toString(),
        text = this.text,
        author = this.author,
        category = this.category,
        timestamp = this.timestamp,
        isFavorite = isFavorite
    )
}

private fun FavoriteQuoteEntity.toQuote(): Quote {
    return Quote(
        id = this.id,
        text = this.text,
        author = this.author,
        category = this.category,
        timestamp = this.timestamp,
        isFavorite = true
    )
}
