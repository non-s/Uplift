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
    
    private val repository = QuoteRepository()
    
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val quote = repository.getRandomQuote()
            
            quote?.let {
                showNotification(it.text, it.author)
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
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
            .setSmallIcon(android.R.drawable.star_on)
            .setContentTitle("Frase do Dia")
            .setContentText(quoteText)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("\"$quoteText\"\n\n— $author"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        notificationManager.notify(1001, notification)
    }
}
