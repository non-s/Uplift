package com.motivacional.frases.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.motivacional.frases.R
import com.motivacional.frases.data.model.Quote

class QuoteAdapter(
    private val onFavoriteClick: (Quote) -> Unit,
    private val onShareClick: (Quote) -> Unit
) : RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder>() {
    
    private var quotes: List<Quote> = emptyList()
    
    fun updateQuotes(newQuotes: List<Quote>) {
        quotes = newQuotes
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quote, parent, false)
        return QuoteViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.bind(quotes[position])
    }
    
    override fun getItemCount() = quotes.size
    
    inner class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textQuote: TextView = itemView.findViewById(R.id.textQuote)
        private val textAuthor: TextView = itemView.findViewById(R.id.textAuthor)
        private val textCategory: TextView = itemView.findViewById(R.id.textCategory)
        private val btnFavorite: ImageButton = itemView.findViewById(R.id.btnFavorite)
        private val btnShare: ImageButton = itemView.findViewById(R.id.btnShare)
        
        fun bind(quote: Quote) {
            textQuote.text = "\"${quote.text}\""
            textAuthor.text = "— ${quote.author}"
            textCategory.text = quote.category.uppercase()
            
            // Ícone de favorito
            val favoriteIcon = if (quote.isFavorite) {
                android.R.drawable.btn_star_big_on
            } else {
                android.R.drawable.btn_star_big_off
            }
            btnFavorite.setImageResource(favoriteIcon)
            
            btnFavorite.setOnClickListener {
                onFavoriteClick(quote)
            }
            
            btnShare.setOnClickListener {
                onShareClick(quote)
            }
        }
    }
}
