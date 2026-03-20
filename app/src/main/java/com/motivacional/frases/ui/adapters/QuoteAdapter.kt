package com.motivacional.frases.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.motivacional.frases.R
import com.motivacional.frases.data.model.Quote
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class QuoteAdapter(
    private val onFavoriteClick: (Quote) -> Unit,
    private val onShareClick: (Quote) -> Unit,
    private val onCopyClick: (Quote) -> Unit
) : RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder>() {

    private var quotes: List<Quote> = emptyList()

    fun updateQuotes(newQuotes: List<Quote>) {
        val diffCallback = QuoteDiffCallback(quotes, newQuotes)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        quotes = newQuotes
        diffResult.dispatchUpdatesTo(this)
    }

    private class QuoteDiffCallback(
        private val oldList: List<Quote>,
        private val newList: List<Quote>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldPos: Int, newPos: Int) =
            oldList[oldPos].id == newList[newPos].id
        override fun areContentsTheSame(oldPos: Int, newPos: Int) =
            oldList[oldPos] == newList[newPos]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quote, parent, false)
        return QuoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.bind(quotes[position])

        // Animação de entrada
        holder.itemView.startAnimation(
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.item_animation)
        )
    }

    override fun getItemCount() = quotes.size

    inner class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textQuote: TextView = itemView.findViewById(R.id.textQuote)
        private val textAuthor: TextView = itemView.findViewById(R.id.textAuthor)
        private val chipCategory: Chip = itemView.findViewById(R.id.chipCategory)
        private val textDate: TextView = itemView.findViewById(R.id.textDate)
        private val btnCopy: MaterialButton = itemView.findViewById(R.id.btnCopy)
        private val btnFavorite: MaterialButton = itemView.findViewById(R.id.btnFavorite)
        private val btnShare: MaterialButton = itemView.findViewById(R.id.btnShare)
        private val accentBar: View = itemView.findViewById(R.id.accentBar)

        fun bind(quote: Quote) {
            textQuote.text = quote.text
            textAuthor.text = quote.author
            chipCategory.text = quote.category.uppercase()

            // Formatar data
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            textDate.text = dateFormat.format(Date(quote.timestamp))

            // Definir cor da barra de acento baseada na categoria
            val accentColor = when (quote.category.lowercase()) {
                "motivação", "motivation" -> R.color.category_motivation_start
                "sucesso", "success" -> R.color.category_success_start
                "amor", "love" -> R.color.category_love_start
                "vida", "life" -> R.color.category_life_start
                "sabedoria", "wisdom" -> R.color.category_wisdom_start
                else -> R.color.primary
            }
            accentBar.setBackgroundResource(accentColor)
            chipCategory.setChipBackgroundColorResource(accentColor)

            // Atualizar ícone e texto do botão de favorito
            if (quote.isFavorite) {
                btnFavorite.setIconResource(android.R.drawable.btn_star_big_on)
                btnFavorite.text = "Favoritado"
            } else {
                btnFavorite.setIconResource(android.R.drawable.btn_star_big_off)
                btnFavorite.text = "Favoritar"
            }

            // Listeners dos botões
            btnCopy.setOnClickListener {
                onCopyClick(quote)
            }

            btnFavorite.setOnClickListener {
                onFavoriteClick(quote)
            }

            btnShare.setOnClickListener {
                onShareClick(quote)
            }
        }
    }
}
