package com.motivacional.frases.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "uplift_preferences"
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_THEME_COLOR = "theme_color"
        private const val KEY_FONT_SIZE = "font_size"
        private const val KEY_STREAK_COUNT = "streak_count"
        private const val KEY_LAST_OPEN_DATE = "last_open_date"
        private const val KEY_TOTAL_QUOTES_READ = "total_quotes_read"
        private const val KEY_APP_OPENS = "app_opens"
        private const val KEY_LAST_QUOTE_TIMESTAMP = "last_quote_timestamp"
        private const val KEY_LAST_QUOTE_ID = "last_quote_id"
    }

    // Dark Mode
    fun isDarkMode(): Boolean = prefs.getBoolean(KEY_DARK_MODE, false)
    fun setDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply()
    }

    // Theme Color
    fun getThemeColor(): String = prefs.getString(KEY_THEME_COLOR, "blue") ?: "blue"
    fun setThemeColor(color: String) {
        prefs.edit().putString(KEY_THEME_COLOR, color).apply()
    }

    // Font Size
    fun getFontSize(): String = prefs.getString(KEY_FONT_SIZE, "medium") ?: "medium"
    fun setFontSize(size: String) {
        prefs.edit().putString(KEY_FONT_SIZE, size).apply()
    }

    // Streaks
    fun getStreakCount(): Int = prefs.getInt(KEY_STREAK_COUNT, 0)
    fun setStreakCount(count: Int) {
        prefs.edit().putInt(KEY_STREAK_COUNT, count).apply()
    }

    fun getLastOpenDate(): Long = prefs.getLong(KEY_LAST_OPEN_DATE, 0L)
    fun setLastOpenDate(timestamp: Long) {
        prefs.edit().putLong(KEY_LAST_OPEN_DATE, timestamp).apply()
    }

    // Statistics
    fun getTotalQuotesRead(): Int = prefs.getInt(KEY_TOTAL_QUOTES_READ, 0)
    fun incrementQuotesRead() {
        val current = getTotalQuotesRead()
        prefs.edit().putInt(KEY_TOTAL_QUOTES_READ, current + 1).apply()
    }

    fun getAppOpens(): Int = prefs.getInt(KEY_APP_OPENS, 0)
    fun incrementAppOpens() {
        val current = getAppOpens()
        prefs.edit().putInt(KEY_APP_OPENS, current + 1).apply()
    }

    // Daily Quote Management
    fun getLastQuoteTimestamp(): Long = prefs.getLong(KEY_LAST_QUOTE_TIMESTAMP, 0L)
    fun saveLastQuoteTimestamp(timestamp: Long) {
        prefs.edit().putLong(KEY_LAST_QUOTE_TIMESTAMP, timestamp).apply()
    }

    fun getLastQuoteId(): String = prefs.getString(KEY_LAST_QUOTE_ID, "") ?: ""
    fun saveLastQuoteId(quoteId: String) {
        prefs.edit().putString(KEY_LAST_QUOTE_ID, quoteId).apply()
    }

    // Clear all preferences
    fun clearAll() {
        prefs.edit().clear().apply()
    }
}
