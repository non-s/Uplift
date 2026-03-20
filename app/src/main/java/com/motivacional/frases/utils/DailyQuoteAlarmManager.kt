package com.motivacional.frases.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.util.Calendar

/**
 * Gerenciador de alarmes para notificações diárias às 8:00 da manhã
 */
object DailyQuoteAlarmManager {

    private const val TAG = "DailyQuoteAlarm"
    private const val ALARM_REQUEST_CODE = 1001

    /**
     * Agenda notificação diária para às 8:00 da manhã
     */
    fun scheduleDailyNotification(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Criar intent para o BroadcastReceiver
        val intent = Intent(context, DailyQuoteReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Calcular próximo horário de 8:00 da manhã
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // Se já passou das 8h hoje, agendar para amanhã
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        Log.d(TAG, "Agendando notificação para: ${calendar.time}")

        // Usar setExactAndAllowWhileIdle para garantir execução no horário exato
        // Mesmo com o dispositivo em modo de economia de energia
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
            Log.d(TAG, "Alarme agendado com sucesso para ${calendar.time}")
        } catch (e: SecurityException) {
            Log.e(TAG, "Erro ao agendar alarme: permissão negada", e)
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao agendar alarme", e)
        }
    }

    /**
     * Cancela a notificação diária agendada
     */
    fun cancelDailyNotification(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyQuoteReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
        Log.d(TAG, "Alarme cancelado")
    }

    /**
     * Verifica se o alarme está agendado
     */
    fun isScheduled(context: Context): Boolean {
        val intent = Intent(context, DailyQuoteReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        return pendingIntent != null
    }
}
