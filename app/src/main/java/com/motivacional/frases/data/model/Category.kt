package com.motivacional.frases.data.model

data class Category(
    val id: String = "",
    val name: String = "",
    val icon: String = ""
)

object Categories {
    val ALL = Category("all", "Todas", "⭐")
    val MOTIVATION = Category("motivation", "Motivação", "💪")
    val SUCCESS = Category("success", "Sucesso", "🏆")
    val LOVE = Category("love", "Amor", "❤️")
    val LIFE = Category("life", "Vida", "🌟")
    val WISDOM = Category("wisdom", "Sabedoria", "🧠")
    
    fun getAll() = listOf(ALL, MOTIVATION, SUCCESS, LOVE, LIFE, WISDOM)
}
