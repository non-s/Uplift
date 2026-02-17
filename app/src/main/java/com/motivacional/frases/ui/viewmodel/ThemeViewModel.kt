package com.motivacional.frases.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.motivacional.frases.utils.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ThemeViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencesManager = PreferencesManager(application)

    private val _isDarkMode = MutableStateFlow(preferencesManager.isDarkMode())
    val isDarkMode: StateFlow<Boolean> = _isDarkMode

    private val _themeColor = MutableStateFlow(preferencesManager.getThemeColor())
    val themeColor: StateFlow<String> = _themeColor

    private val _fontSize = MutableStateFlow(preferencesManager.getFontSize())
    val fontSize: StateFlow<String> = _fontSize

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setDarkMode(enabled)
            _isDarkMode.value = enabled
        }
    }

    fun setThemeColor(color: String) {
        viewModelScope.launch {
            preferencesManager.setThemeColor(color)
            _themeColor.value = color
        }
    }

    fun setFontSize(size: String) {
        viewModelScope.launch {
            preferencesManager.setFontSize(size)
            _fontSize.value = size
        }
    }
}
