package com.motivacional.frases.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.motivacional.frases.R
import com.motivacional.frases.data.repository.QuoteRepository
import com.motivacional.frases.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuoteWidget : AppWidgetProvider() {

    companion object {
        private const val ACTION_REFRESH = "com.motivacional.frases.ACTION_REFRESH"
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (intent?.action == ACTION_REFRESH && context != null) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                android.content.ComponentName(context, QuoteWidget::class.java)
            )
            onUpdate(context, appWidgetManager, appWidgetIds)
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_quote)

        // Intent para abrir o app
        val appIntent = Intent(context, MainActivity::class.java)
        val appPendingIntent = PendingIntent.getActivity(
            context, 0, appIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_quote_text, appPendingIntent)

        // Intent para atualizar o widget
        val refreshIntent = Intent(context, QuoteWidget::class.java).apply {
            action = ACTION_REFRESH
        }
        val refreshPendingIntent = PendingIntent.getBroadcast(
            context, 0, refreshIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_refresh_button, refreshPendingIntent)

        // Mostrar placeholder antes de lançar a coroutine para evitar race condition
        views.setTextViewText(R.id.widget_quote_text, "Carregando frase...")
        views.setTextViewText(R.id.widget_quote_author, "")
        appWidgetManager.updateAppWidget(appWidgetId, views)

        // Buscar uma frase aleatória
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = QuoteRepository(context)
                val quote = repository.getRandomQuote()

                quote?.let {
                    views.setTextViewText(R.id.widget_quote_text, "\"${it.text}\"")
                    views.setTextViewText(R.id.widget_quote_author, "— ${it.author}")
                } ?: run {
                    views.setTextViewText(
                        R.id.widget_quote_text,
                        "Abra o app para ver frases inspiradoras!"
                    )
                    views.setTextViewText(R.id.widget_quote_author, "")
                }

                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: Exception) {
                e.printStackTrace()
                views.setTextViewText(
                    R.id.widget_quote_text,
                    "Toque aqui para abrir o app"
                )
                views.setTextViewText(R.id.widget_quote_author, "")
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }

    override fun onEnabled(context: Context) {
        // Primeiro widget adicionado
    }

    override fun onDisabled(context: Context) {
        // Último widget removido
    }
}
