package com.motivacional.frases.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Receiver que reagenda o alarme diário quando o dispositivo é reiniciado
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("BootReceiver", "Dispositivo reiniciado, reagendando notificação diária")
            DailyQuoteAlarmManager.scheduleDailyNotification(context)
        }
    }
}
