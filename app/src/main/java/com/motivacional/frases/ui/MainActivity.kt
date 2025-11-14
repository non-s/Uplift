package com.motivacional.frases.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.ads.AdView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.motivacional.frases.R
import com.motivacional.frases.data.model.Categories
import com.motivacional.frases.data.model.Quote
import com.motivacional.frases.ui.adapters.QuoteAdapter
import com.motivacional.frases.ui.viewmodel.QuoteViewModel
import com.motivacional.frases.utils.AdMobHelper
import com.motivacional.frases.utils.DailyQuoteWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    
    private val viewModel: QuoteViewModel by viewModels()
    private lateinit var adMobHelper: AdMobHelper
    private lateinit var quoteAdapter: QuoteAdapter
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var chipGroup: ChipGroup
    private lateinit var adView: AdView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        setupViews()
        setupAds()
        setupRecyclerView()
        setupCategories()
        setupObservers()
        setupDailyNotifications()
    }
    
    private fun setupViews() {
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        chipGroup = findViewById(R.id.chipGroup)
        adView = findViewById(R.id.adView)
    }
    
    private fun setupAds() {
        adMobHelper = AdMobHelper(this)
        adMobHelper.initialize()
        adMobHelper.loadBannerAd(adView)
        adMobHelper.loadInterstitialAd()
    }
    
    private fun setupRecyclerView() {
        quoteAdapter = QuoteAdapter(
            onFavoriteClick = { quote ->
                viewModel.toggleFavorite(quote)
                adMobHelper.showInterstitialAdIfReady(this)
            },
            onShareClick = { quote ->
                shareQuote(quote)
                adMobHelper.showInterstitialAdIfReady(this)
            }
        )
        
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = quoteAdapter
        }
    }
    
    private fun setupCategories() {
        Categories.getAll().forEach { category ->
            val chip = Chip(this).apply {
                text = "${category.icon} ${category.name}"
                isCheckable = true
                isChecked = category.id == "all"
                setOnClickListener {
                    if (category.id == "favorites") {
                        viewModel.loadFavorites()
                    } else {
                        viewModel.loadQuotes(category.id)
                    }
                }
            }
            chipGroup.addView(chip)
        }
        
        // Adicionar chip de favoritos
        val favChip = Chip(this).apply {
            text = "⭐ Favoritos"
            isCheckable = true
            setOnClickListener {
                viewModel.loadFavorites()
            }
        }
        chipGroup.addView(favChip)
    }
    
    private fun setupObservers() {
        viewModel.quotes.observe(this) { quotes ->
            quoteAdapter.updateQuotes(quotes)
        }
        
        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
    
    private fun shareQuote(quote: Quote) {
        val shareText = "\"${quote.text}\"\n\n— ${quote.author}\n\n" +
                "Compartilhado via Frases Motivacionais"
        
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        
        startActivity(Intent.createChooser(intent, "Compartilhar frase"))
    }
    
    private fun setupDailyNotifications() {
        val dailyWorkRequest = PeriodicWorkRequestBuilder<DailyQuoteWorker>(
            1, TimeUnit.DAYS
        ).build()
        
        WorkManager.getInstance(this).enqueue(dailyWorkRequest)
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_initialize_db -> {
                viewModel.initializeDatabase()
                Toast.makeText(this, "Database inicializado com frases!", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    override fun onResume() {
        super.onResume()
        adView.resume()
    }
    
    override fun onPause() {
        super.onPause()
        adView.pause()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        adView.destroy()
    }
}
