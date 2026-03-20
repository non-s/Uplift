package com.motivacional.frases.ui

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.motivacional.frases.R
import com.motivacional.frases.data.model.Categories
import com.motivacional.frases.data.model.Quote
import com.motivacional.frases.ui.adapters.QuoteAdapter
import com.motivacional.frases.ui.viewmodel.QuoteViewModel
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import com.google.android.material.snackbar.Snackbar
import com.motivacional.frases.utils.AdMobHelper
import com.motivacional.frases.utils.PreferencesManager
import com.motivacional.frases.utils.ThemeHelper
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    private val viewModel: QuoteViewModel by viewModels()
    private lateinit var quoteAdapter: QuoteAdapter
    private lateinit var adMobHelper: AdMobHelper
    private lateinit var preferencesManager: PreferencesManager

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var chipGroupFilter: ChipGroup
    private lateinit var emptyState: LinearLayout

    private var currentSearchQuery: String = ""
    private var currentCategory: String = "all"
    private var allQuotes: List<Quote> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        preferencesManager = PreferencesManager(this)

        // Aplicar tema antes de setContentView
        applyTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setupToolbar()
        setupViews()
        setupRecyclerView()
        setupCategoryFilters()
        setupObservers()
        setupAds()

        // Aplicar cores do tema atual
        ThemeHelper.applyThemeToSearch(this, preferencesManager.getThemeColor())
    }

    private fun applyTheme() {
        // Aplicar modo escuro
        if (preferencesManager.isDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        // Aplicar tema de cor
        val themeColor = preferencesManager.getThemeColor()
        when (themeColor) {
            "purple" -> setTheme(R.style.Theme_FrasesMotivacionais_Purple)
            "green" -> setTheme(R.style.Theme_FrasesMotivacionais_Green)
            "orange" -> setTheme(R.style.Theme_FrasesMotivacionais_Orange)
            "pink" -> setTheme(R.style.Theme_FrasesMotivacionais_Pink)
            "red" -> setTheme(R.style.Theme_FrasesMotivacionais_Red)
            else -> setTheme(R.style.Theme_FrasesMotivacionais)
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Buscar Frases"

        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupViews() {
        searchView = findViewById(R.id.searchView)
        recyclerView = findViewById(R.id.recyclerView)
        chipGroupFilter = findViewById(R.id.chipGroupFilter)
        emptyState = findViewById(R.id.emptyState)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { performSearch(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                currentSearchQuery = newText ?: ""
                performSearch(currentSearchQuery)
                return true
            }
        })

        // Expandir search view por padrão
        searchView.isIconified = false
    }

    private fun setupRecyclerView() {
        quoteAdapter = QuoteAdapter(
            onFavoriteClick = { quote ->
                viewModel.toggleFavorite(quote)
                val message = if (quote.isFavorite) {
                    getString(R.string.favorite_added)
                } else {
                    getString(R.string.favorite_removed)
                }
                showSnackbar(message)
                adMobHelper.showInterstitialAdIfReady(this)
            },
            onShareClick = { quote ->
                shareQuote(quote)
                adMobHelper.showInterstitialAdIfReady(this)
            },
            onCopyClick = { quote ->
                copyQuoteToClipboard(quote)
                showSnackbar(getString(R.string.quote_copied))
            }
        )

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = quoteAdapter
        }
    }

    private fun setupCategoryFilters() {
        // Adicionar chip "Todas"
        val allChip = Chip(this).apply {
            text = "📚 Todas"
            isCheckable = true
            isChecked = true
            setOnClickListener {
                currentCategory = "all"
                performSearch(currentSearchQuery)
            }
        }
        chipGroupFilter.addView(allChip)

        // Adicionar chips de categorias
        Categories.getAll().forEach { category ->
            if (category.id != "all") {
                val chip = Chip(this).apply {
                    text = "${category.icon} ${category.name}"
                    isCheckable = true
                    setOnClickListener {
                        currentCategory = category.id
                        performSearch(currentSearchQuery)
                    }
                }
                chipGroupFilter.addView(chip)
            }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.quotes.collectLatest { quotes ->
                allQuotes = quotes
                performSearch(currentSearchQuery)
            }
        }
    }

    private fun setupAds() {
        adMobHelper = AdMobHelper(this)
        adMobHelper.initialize()
        adMobHelper.loadInterstitialAd()
    }

    private fun performSearch(query: String) {
        val filteredQuotes = allQuotes.filter { quote ->
            // Filtrar por categoria se não for "all"
            val matchesCategory = currentCategory == "all" || quote.category == currentCategory

            // Filtrar por texto de busca
            val matchesQuery = if (query.isBlank()) {
                true
            } else {
                quote.text.contains(query, ignoreCase = true) ||
                        quote.author.contains(query, ignoreCase = true)
            }

            matchesCategory && matchesQuery
        }

        quoteAdapter.updateQuotes(filteredQuotes)

        // Mostrar/ocultar estado vazio
        if (filteredQuotes.isEmpty()) {
            emptyState.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyState.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun shareQuote(quote: Quote) {
        val shareText = getString(R.string.share_template, quote.text, quote.author)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_SUBJECT, "Frase Motivacional")
        }

        startActivity(Intent.createChooser(intent, getString(R.string.share_quote)))
    }

    private fun copyQuoteToClipboard(quote: Quote) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(
            "quote",
            "\"${quote.text}\"\n\n— ${quote.author}"
        )
        clipboard.setPrimaryClip(clip)
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(
            findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}
