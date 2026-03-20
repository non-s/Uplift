package com.motivacional.frases.data.model

import com.google.firebase.firestore.DocumentId

data class Quote(
    @DocumentId
    val id: String = "",
    val text: String = "",
    val author: String = "",
    val category: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    var isFavorite: Boolean = false
)
