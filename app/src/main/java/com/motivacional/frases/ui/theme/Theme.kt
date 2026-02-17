package com.motivacional.frases.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.motivacional.frases.ui.viewmodel.ThemeViewModel

// Default Blue Theme
private val BlueLightColorScheme = lightColorScheme(
    primary = Color(0xFF2196F3),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBBDEFB),
    onPrimaryContainer = Color(0xFF0D47A1),
    secondary = Color(0xFF03A9F4),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFB3E5FC),
    onSecondaryContainer = Color(0xFF01579B),
    tertiary = Color(0xFF03A9F4),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFB3E5FC),
    onTertiaryContainer = Color(0xFF01579B),
    error = Color(0xFFEF4444),
    onError = Color.White,
    errorContainer = Color(0xFFFFCDD2),
    onErrorContainer = Color(0xFFB71C1C),
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF1E293B),
    surface = Color.White,
    onSurface = Color(0xFF1E293B),
    surfaceVariant = Color(0xFFE0E0E0),
    onSurfaceVariant = Color(0xFF424242),
    outline = Color(0xFFBDBDBD),
    inverseOnSurface = Color(0xFFF5F5F5),
    inverseSurface = Color(0xFF303030),
    inversePrimary = Color(0xFF90CAF9),
    surfaceTint = Color(0xFF2196F3),
    outlineVariant = Color(0xFFE0E0E0),
    scrim = Color(0xFF000000)
)

private val BlueDarkColorScheme = darkColorScheme(
    primary = Color(0xFF90CAF9),
    onPrimary = Color(0xFF0D47A1),
    primaryContainer = Color(0xFF1976D2),
    onPrimaryContainer = Color(0xFFBBDEFB),
    secondary = Color(0xFF81D4FA),
    onSecondary = Color(0xFF01579B),
    secondaryContainer = Color(0xFF03A9F4),
    onSecondaryContainer = Color(0xFFB3E5FC),
    tertiary = Color(0xFF81D4FA),
    onTertiary = Color(0xFF01579B),
    tertiaryContainer = Color(0xFF03A9F4),
    onTertiaryContainer = Color(0xFFB3E5FC),
    error = Color(0xFFFFCDD2),
    onError = Color(0xFFB71C1C),
    errorContainer = Color(0xFFEF4444),
    onErrorContainer = Color(0xFFFFCDD2),
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF1F5F9),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF424242),
    onSurfaceVariant = Color(0xFFE0E0E0),
    outline = Color(0xFF757575),
    inverseOnSurface = Color(0xFF303030),
    inverseSurface = Color(0xFFF5F5F5),
    inversePrimary = Color(0xFF2196F3),
    surfaceTint = Color(0xFF90CAF9),
    outlineVariant = Color(0xFF424242),
    scrim = Color(0xFF000000)
)

// Purple Theme
private val PurpleLightColorScheme = lightColorScheme(
    primary = Color(0xFF9C27B0),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE1BEE7),
    onPrimaryContainer = Color(0xFF4A148C),
    secondary = Color(0xFFE040FB),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF3E5F5),
    onSecondaryContainer = Color(0xFF8E24AA),
    tertiary = Color(0xFFE040FB),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFF3E5F5),
    onTertiaryContainer = Color(0xFF8E24AA),
    error = Color(0xFFEF4444),
    onError = Color.White,
    errorContainer = Color(0xFFFFCDD2),
    onErrorContainer = Color(0xFFB71C1C),
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF1E293B),
    surface = Color.White,
    onSurface = Color(0xFF1E293B),
    surfaceVariant = Color(0xFFE0E0E0),
    onSurfaceVariant = Color(0xFF424242),
    outline = Color(0xFFBDBDBD),
    inverseOnSurface = Color(0xFFF5F5F5),
    inverseSurface = Color(0xFF303030),
    inversePrimary = Color(0xFFCE93D8),
    surfaceTint = Color(0xFF9C27B0),
    outlineVariant = Color(0xFFE0E0E0),
    scrim = Color(0xFF000000)
)

private val PurpleDarkColorScheme = darkColorScheme(
    primary = Color(0xFFCE93D8),
    onPrimary = Color(0xFF4A148C),
    primaryContainer = Color(0xFF7B1FA2),
    onPrimaryContainer = Color(0xFFE1BEE7),
    secondary = Color(0xFFEA80FC),
    onSecondary = Color(0xFF8E24AA),
    secondaryContainer = Color(0xFFE040FB),
    onSecondaryContainer = Color(0xFFF3E5F5),
    tertiary = Color(0xFFEA80FC),
    onTertiary = Color(0xFF8E24AA),
    tertiaryContainer = Color(0xFFE040FB),
    onTertiaryContainer = Color(0xFFF3E5F5),
    error = Color(0xFFFFCDD2),
    onError = Color(0xFFB71C1C),
    errorContainer = Color(0xFFEF4444),
    onErrorContainer = Color(0xFFFFCDD2),
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF1F5F9),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF424242),
    onSurfaceVariant = Color(0xFFE0E0E0),
    outline = Color(0xFF757575),
    inverseOnSurface = Color(0xFF303030),
    inverseSurface = Color(0xFFF5F5F5),
    inversePrimary = Color(0xFF9C27B0),
    surfaceTint = Color(0xFFCE93D8),
    outlineVariant = Color(0xFF424242),
    scrim = Color(0xFF000000)
)

// Green Theme
private val GreenLightColorScheme = lightColorScheme(
    primary = Color(0xFF4CAF50),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFC8E6C9),
    onPrimaryContainer = Color(0xFF1B5E20),
    secondary = Color(0xFF8BC34A),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDCEDC8),
    onSecondaryContainer = Color(0xFF558B2F),
    tertiary = Color(0xFF8BC34A),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFDCEDC8),
    onTertiaryContainer = Color(0xFF558B2F),
    error = Color(0xFFEF4444),
    onError = Color.White,
    errorContainer = Color(0xFFFFCDD2),
    onErrorContainer = Color(0xFFB71C1C),
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF1E293B),
    surface = Color.White,
    onSurface = Color(0xFF1E293B),
    surfaceVariant = Color(0xFFE0E0E0),
    onSurfaceVariant = Color(0xFF424242),
    outline = Color(0xFFBDBDBD),
    inverseOnSurface = Color(0xFFF5F5F5),
    inverseSurface = Color(0xFF303030),
    inversePrimary = Color(0xFFA5D6A7),
    surfaceTint = Color(0xFF4CAF50),
    outlineVariant = Color(0xFFE0E0E0),
    scrim = Color(0xFF000000)
)

private val GreenDarkColorScheme = darkColorScheme(
    primary = Color(0xFFA5D6A7),
    onPrimary = Color(0xFF1B5E20),
    primaryContainer = Color(0xFF388E3C),
    onPrimaryContainer = Color(0xFFC8E6C9),
    secondary = Color(0xFFC5E1A5),
    onSecondary = Color(0xFF558B2F),
    secondaryContainer = Color(0xFF8BC34A),
    onSecondaryContainer = Color(0xFFDCEDC8),
    tertiary = Color(0xFFC5E1A5),
    onTertiary = Color(0xFF558B2F),
    tertiaryContainer = Color(0xFF8BC34A),
    onTertiaryContainer = Color(0xFFDCEDC8),
    error = Color(0xFFFFCDD2),
    onError = Color(0xFFB71C1C),
    errorContainer = Color(0xFFEF4444),
    onErrorContainer = Color(0xFFFFCDD2),
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF1F5F9),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF424242),
    onSurfaceVariant = Color(0xFFE0E0E0),
    outline = Color(0xFF757575),
    inverseOnSurface = Color(0xFF303030),
    inverseSurface = Color(0xFFF5F5F5),
    inversePrimary = Color(0xFF4CAF50),
    surfaceTint = Color(0xFFA5D6A7),
    outlineVariant = Color(0xFF424242),
    scrim = Color(0xFF000000)
)

// Orange Theme
private val OrangeLightColorScheme = lightColorScheme(
    primary = Color(0xFFFF9800),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFECB3),
    onPrimaryContainer = Color(0xFFE65100),
    secondary = Color(0xFFFFB74D),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE0B2),
    onSecondaryContainer = Color(0xFFEF6C00),
    tertiary = Color(0xFFFFB74D),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFE0B2),
    onTertiaryContainer = Color(0xFFEF6C00),
    error = Color(0xFFEF4444),
    onError = Color.White,
    errorContainer = Color(0xFFFFCDD2),
    onErrorContainer = Color(0xFFB71C1C),
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF1E293B),
    surface = Color.White,
    onSurface = Color(0xFF1E293B),
    surfaceVariant = Color(0xFFE0E0E0),
    onSurfaceVariant = Color(0xFF424242),
    outline = Color(0xFFBDBDBD),
    inverseOnSurface = Color(0xFFF5F5F5),
    inverseSurface = Color(0xFF303030),
    inversePrimary = Color(0xFFFFCC80),
    surfaceTint = Color(0xFFFF9800),
    outlineVariant = Color(0xFFE0E0E0),
    scrim = Color(0xFF000000)
)

private val OrangeDarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFCC80),
    onPrimary = Color(0xFFE65100),
    primaryContainer = Color(0xFFF57C00),
    onPrimaryContainer = Color(0xFFFFECB3),
    secondary = Color(0xFFFFE0B2),
    onSecondary = Color(0xFFEF6C00),
    secondaryContainer = Color(0xFFFFB74D),
    onSecondaryContainer = Color(0xFFFFE0B2),
    tertiary = Color(0xFFFFE0B2),
    onTertiary = Color(0xFFEF6C00),
    tertiaryContainer = Color(0xFFFFB74D),
    onTertiaryContainer = Color(0xFFFFE0B2),
    error = Color(0xFFFFCDD2),
    onError = Color(0xFFB71C1C),
    errorContainer = Color(0xFFEF4444),
    onErrorContainer = Color(0xFFFFCDD2),
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF1F5F9),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF424242),
    onSurfaceVariant = Color(0xFFE0E0E0),
    outline = Color(0xFF757575),
    inverseOnSurface = Color(0xFF303030),
    inverseSurface = Color(0xFFF5F5F5),
    inversePrimary = Color(0xFFFF9800),
    surfaceTint = Color(0xFFFFCC80),
    outlineVariant = Color(0xFF424242),
    scrim = Color(0xFF000000)
)

// Pink Theme
private val PinkLightColorScheme = lightColorScheme(
    primary = Color(0xFFE91E63),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFF8BBD0),
    onPrimaryContainer = Color(0xFF880E4F),
    secondary = Color(0xFFFF4081),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFF80AB),
    onSecondaryContainer = Color(0xFFC51162),
    tertiary = Color(0xFFFF4081),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFF80AB),
    onTertiaryContainer = Color(0xFFC51162),
    error = Color(0xFFEF4444),
    onError = Color.White,
    errorContainer = Color(0xFFFFCDD2),
    onErrorContainer = Color(0xFFB71C1C),
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF1E293B),
    surface = Color.White,
    onSurface = Color(0xFF1E293B),
    surfaceVariant = Color(0xFFE0E0E0),
    onSurfaceVariant = Color(0xFF424242),
    outline = Color(0xFFBDBDBD),
    inverseOnSurface = Color(0xFFF5F5F5),
    inverseSurface = Color(0xFF303030),
    inversePrimary = Color(0xFFF48FB1),
    surfaceTint = Color(0xFFE91E63),
    outlineVariant = Color(0xFFE0E0E0),
    scrim = Color(0xFF000000)
)

private val PinkDarkColorScheme = darkColorScheme(
    primary = Color(0xFFF48FB1),
    onPrimary = Color(0xFF880E4F),
    primaryContainer = Color(0xFFC2185B),
    onPrimaryContainer = Color(0xFFF8BBD0),
    secondary = Color(0xFFFF80AB),
    onSecondary = Color(0xFFC51162),
    secondaryContainer = Color(0xFFFF4081),
    onSecondaryContainer = Color(0xFFFF80AB),
    tertiary = Color(0xFFFF80AB),
    onTertiary = Color(0xFFC51162),
    tertiaryContainer = Color(0xFFFF4081),
    onTertiaryContainer = Color(0xFFFF80AB),
    error = Color(0xFFFFCDD2),
    onError = Color(0xFFB71C1C),
    errorContainer = Color(0xFFEF4444),
    onErrorContainer = Color(0xFFFFCDD2),
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF1F5F9),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF424242),
    onSurfaceVariant = Color(0xFFE0E0E0),
    outline = Color(0xFF757575),
    inverseOnSurface = Color(0xFF303030),
    inverseSurface = Color(0xFFF5F5F5),
    inversePrimary = Color(0xFFE91E63),
    surfaceTint = Color(0xFFF48FB1),
    outlineVariant = Color(0xFF424242),
    scrim = Color(0xFF000000)
)

// Red Theme
private val RedLightColorScheme = lightColorScheme(
    primary = Color(0xFFF44336),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFCDD2),
    onPrimaryContainer = Color(0xFFB71C1C),
    secondary = Color(0xFFFF5252),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFF8A80),
    onSecondaryContainer = Color(0xFFD50000),
    tertiary = Color(0xFFFF5252),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFF8A80),
    onTertiaryContainer = Color(0xFFD50000),
    error = Color(0xFFEF4444),
    onError = Color.White,
    errorContainer = Color(0xFFFFCDD2),
    onErrorContainer = Color(0xFFB71C1C),
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF1E293B),
    surface = Color.White,
    onSurface = Color(0xFF1E293B),
    surfaceVariant = Color(0xFFE0E0E0),
    onSurfaceVariant = Color(0xFF424242),
    outline = Color(0xFFBDBDBD),
    inverseOnSurface = Color(0xFFF5F5F5),
    inverseSurface = Color(0xFF303030),
    inversePrimary = Color(0xFFFF8A80),
    surfaceTint = Color(0xFFF44336),
    outlineVariant = Color(0xFFE0E0E0),
    scrim = Color(0xFF000000)
)

private val RedDarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF8A80),
    onPrimary = Color(0xFFB71C1C),
    primaryContainer = Color(0xFFD32F2F),
    onPrimaryContainer = Color(0xFFFFCDD2),
    secondary = Color(0xFFFF8A80),
    onSecondary = Color(0xFFD50000),
    secondaryContainer = Color(0xFFFF5252),
    onSecondaryContainer = Color(0xFFFF8A80),
    tertiary = Color(0xFFFF8A80),
    onTertiary = Color(0xFFD50000),
    tertiaryContainer = Color(0xFFFF5252),
    onTertiaryContainer = Color(0xFFFF8A80),
    error = Color(0xFFFFCDD2),
    onError = Color(0xFFB71C1C),
    errorContainer = Color(0xFFEF4444),
    onErrorContainer = Color(0xFFFFCDD2),
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF1F5F9),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF424242),
    onSurfaceVariant = Color(0xFFE0E0E0),
    outline = Color(0xFF757575),
    inverseOnSurface = Color(0xFF303030),
    inverseSurface = Color(0xFFF5F5F5),
    inversePrimary = Color(0xFFF44336),
    surfaceTint = Color(0xFFFF8A80),
    outlineVariant = Color(0xFF424242),
    scrim = Color(0xFF000000)
)

@Composable
fun FrasesMotivacionaisTheme(
    themeViewModel: ThemeViewModel = viewModel(),
    content: @Composable () -> Unit
) {
    val darkTheme by themeViewModel.isDarkMode.collectAsStateWithLifecycle()
    val themeColor by themeViewModel.themeColor.collectAsStateWithLifecycle()
    val fontSize by themeViewModel.fontSize.collectAsStateWithLifecycle()

    val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val context = LocalContext.current

    val colorScheme = when {
        dynamicColor && darkTheme -> dynamicDarkColorScheme(context)
        dynamicColor && !darkTheme -> dynamicLightColorScheme(context)
        darkTheme -> when (themeColor) {
            "purple" -> PurpleDarkColorScheme
            "green" -> GreenDarkColorScheme
            "orange" -> OrangeDarkColorScheme
            "pink" -> PinkDarkColorScheme
            "red" -> RedDarkColorScheme
            else -> BlueDarkColorScheme // Default
        }
        else -> when (themeColor) {
            "purple" -> PurpleLightColorScheme
            "green" -> GreenLightColorScheme
            "orange" -> OrangeLightColorScheme
            "pink" -> PinkLightColorScheme
            "red" -> RedLightColorScheme
            else -> BlueLightColorScheme // Default
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = getTypography(fontSize),
        content = content
    )
}