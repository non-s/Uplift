package com.motivacional.frases.ui

import android.app.Activity
import android.app.Application
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.motivacional.frases.ui.theme.FrasesMotivacionaisTheme
import com.motivacional.frases.ui.viewmodel.ThemeViewModel
import com.motivacional.frases.utils.DailyQuoteWorker
import com.motivacional.frases.utils.PreferencesManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

class SettingsActivity : ComponentActivity() {

    private val themeViewModel: ThemeViewModel by viewModels()
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferencesManager = PreferencesManager(this)
        setContent {
            FrasesMotivacionaisTheme(themeViewModel = themeViewModel) {
                SettingsScreen(themeViewModel = themeViewModel, preferencesManager = preferencesManager)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(themeViewModel: ThemeViewModel, preferencesManager: PreferencesManager) {
    val context = LocalContext.current
    val activity = context as? Activity

    val isDarkMode by themeViewModel.isDarkMode.collectAsState()
    val selectedColor by themeViewModel.themeColor.collectAsState()
    val selectedFontSize by themeViewModel.fontSize.collectAsState()



    var showColorDialog by remember { mutableStateOf(false) }
    var showFontSizeDialog by remember { mutableStateOf(false) }

    if (showColorDialog) {
        ColorSelectionDialog(
            currentTheme = selectedColor,
            onThemeSelected = { color ->
                themeViewModel.setThemeColor(color)
                showColorDialog = false
            },
            onDismiss = { showColorDialog = false }
        )
    }

    if (showFontSizeDialog) {
        FontSizeSelectionDialog(
            currentSize = selectedFontSize,
            onSizeSelected = { size ->
                themeViewModel.setFontSize(size)
                showFontSizeDialog = false
            },
            onDismiss = { showFontSizeDialog = false }
        )
    }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configurações") },
                navigationIcon = {
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SettingItem(
                title = "Modo Escuro",
                subtitle = "Economize bateria e reduza o cansaço visual",
                control = {
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { checked ->
                            themeViewModel.setDarkMode(checked)
                            AppCompatDelegate.setDefaultNightMode(
                                if (checked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                            )
                        }
                    )
                }
            )
            HorizontalDivider()
            SettingItem(
                title = "🎨 Tema de Cores",
                subtitle = "Personalize as cores do aplicativo",
                control = { },
                onClick = { showColorDialog = true }
            )
            HorizontalDivider()
            SettingItem(
                title = "📝 Tamanho da Fonte",
                subtitle = "Ajuste o tamanho do texto para melhor leitura",
                control = { },
                onClick = { showFontSizeDialog = true }
            )
            HorizontalDivider()
        }
    }
}

@Composable
private fun FontSizeSelectionDialog(
    currentSize: String,
    onSizeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sizeOptions = listOf(
        "small" to "Pequeno",
        "medium" to "Médio (Padrão)",
        "large" to "Grande",
        "xlarge" to "Extra Grande"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Escolha um tamanho de fonte") },
        text = {
            Column {
                sizeOptions.forEach { (size, label) ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onSizeSelected(size) }
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (size == currentSize),
                                onClick = null
                            )
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Fechar")
            }
        }
    )
}

@Composable
private fun ColorSelectionDialog(
    currentTheme: String,
    onThemeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val colorOptions = listOf(
        "blue" to "🔵 Azul (Padrão)",
        "purple" to "🟣 Roxo",
        "green" to "🟢 Verde",
        "orange" to "🟠 Laranja",
        "pink" to "🩷 Rosa",
        "red" to "🔴 Vermelho"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Escolha uma cor de tema") },
        text = {
            Column {
                colorOptions.forEach { (theme, label) ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onThemeSelected(theme) }
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (theme == currentTheme),
                                onClick = null
                            )
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Fechar")
            }
        }
    )
}


@Composable
fun SettingItem(
    title: String,
    subtitle: String,
    control: @Composable () -> Unit,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    if (onClick != null && enabled) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick,
            enabled = true
        ) {
            SettingItemContent(title = title, subtitle = subtitle, enabled = true, control = control)
        }
    } else {
        Surface(
            modifier = Modifier.fillMaxWidth()
        ) {
            SettingItemContent(title = title, subtitle = subtitle, enabled = enabled, control = control)
        }
    }
}

@Composable
private fun SettingItemContent(
    title: String,
    subtitle: String,
    enabled: Boolean,
    control: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = if (enabled) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
            )
        }
        control()
    }
}

/*
@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    FrasesMotivacionaisTheme {
        SettingsScreen(themeViewModel = ThemeViewModel(LocalContext.current.applicationContext as Application), preferencesManager = PreferencesManager(LocalContext.current))
    }
}
*/
