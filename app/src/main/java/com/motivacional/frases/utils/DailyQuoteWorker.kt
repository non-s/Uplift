package com.motivacional.frases.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.motivacional.frases.data.repository.QuoteRepository
import com.motivacional.frases.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DailyQuoteWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val repository = QuoteRepository(context)
    private val preferencesManager = PreferencesManager(context)

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val currentTime = System.currentTimeMillis()
            val lastQuoteTime = preferencesManager.getLastQuoteTimestamp()
            val lastQuoteId = preferencesManager.getLastQuoteId()

            // Verifica se é um novo dia (compara apenas a data, não o horário)
            val shouldLoadNewQuote = !isSameDay(currentTime, lastQuoteTime)

            if (shouldLoadNewQuote || lastQuoteId.isEmpty()) {
                // Carregar nova frase aleatória
                val quote = repository.getRandomQuote()

                quote?.let {
                    // Salvar timestamp e ID da nova frase
                    preferencesManager.saveLastQuoteTimestamp(currentTime)
                    preferencesManager.saveLastQuoteId(it.id)

                    // Enviar notificação
                    showNotification(it.text, it.author)
                }
            }
            // Se ainda é o mesmo dia, não faz nada

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    /**
     * Verifica se duas timestamps são do mesmo dia
     */
    private fun isSameDay(time1: Long, time2: Long): Boolean {
        if (time2 == 0L) return false
        val cal1 = java.util.Calendar.getInstance().apply { timeInMillis = time1 }
        val cal2 = java.util.Calendar.getInstance().apply { timeInMillis = time2 }
        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
               cal1.get(java.util.Calendar.DAY_OF_YEAR) == cal2.get(java.util.Calendar.DAY_OF_YEAR)
    }
    
    private fun showNotification(quoteText: String, author: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "daily_quote_channel"
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Frase Diária",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Receba uma frase motivacional todos os dias"
            }
            notificationManager.createNotificationChannel(channel)
        }
        
        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(com.motivacional.frases.R.mipmap.ic_launcher)
            .setContentTitle("✨ Sua Frase do Dia Chegou!")
            .setContentText(quoteText)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("\"$quoteText\"\n\n— $author"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 200, 100, 200))
            .build()
        
        notificationManager.notify(1001, notification)
    }
}
