package com.motivacional.frases.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.motivacional.frases.R
import com.motivacional.frases.data.repository.QuoteRepository
import com.motivacional.frases.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * BroadcastReceiver que recebe o alarme diário às 8:00 e envia a notificação
 */
class DailyQuoteReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Usar goAsync para permitir operações assíncronas
        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = QuoteRepository(context)
                val preferencesManager = PreferencesManager(context)

                // Obter frase aleatória
                val quote = repository.getRandomQuote()

                quote?.let {
                    // Salvar como frase do dia
                    preferencesManager.saveLastQuoteTimestamp(System.currentTimeMillis())
                    preferencesManager.saveLastQuoteId(it.id)

                    // Enviar notificação
                    withContext(Dispatchers.Main) {
                        showNotification(context, it.text, it.author)
                    }
                }

                // Reagendar para o próximo dia
                DailyQuoteAlarmManager.scheduleDailyNotification(context)
            } finally {
                pendingResult.finish()
            }
        }
    }

    private fun showNotification(context: Context, quoteText: String, author: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "daily_quote_channel"

        // Criar canal de notificação (Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Frase Diária",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Receba uma frase motivacional todos os dias às 8:00"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 200, 100, 200)
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Intent para abrir o app ao clicar na notificação
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Construir notificação
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("✨ Sua Frase do Dia Chegou!")
            .setContentText(quoteText)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("\"$quoteText\"\n\n— $author")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 200, 100, 200))
            .build()

        notificationManager.notify(1001, notification)
    }
}
