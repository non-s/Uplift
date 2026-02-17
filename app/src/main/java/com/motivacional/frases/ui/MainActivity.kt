package com.motivacional.frases.ui

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.motivacional.frases.R
import com.motivacional.frases.data.model.Quote
import com.motivacional.frases.ui.theme.FrasesMotivacionaisTheme
import com.motivacional.frases.ui.viewmodel.QuoteViewModel
import com.motivacional.frases.utils.DailyQuoteAlarmManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrasesMotivacionaisTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: QuoteViewModel = viewModel()) {
    val context = LocalContext.current
    val quote by viewModel.quote.collectAsStateWithLifecycle()

    // Solicitar permissão de notificação para Android 13+
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Agendar alarme diário às 8:00 após permissão concedida
            DailyQuoteAlarmManager.scheduleDailyNotification(context)
        }
    }

    // Gradiente bonito e moderno
    val gradientColors = listOf(
        Color(0xFF667eea), // Roxo suave
        Color(0xFF764ba2), // Roxo médio
        Color(0xFFf093fb), // Rosa claro
        Color(0xFF4facfe)  // Azul claro
    )

    LaunchedEffect(key1 = true) {
        // Carregar frase do dia
        viewModel.loadDailyQuote()

        // Verificar e solicitar permissão de notificação
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permissão já concedida, agendar alarme diário às 8:00
                    DailyQuoteAlarmManager.scheduleDailyNotification(context)
                }
                else -> {
                    // Solicitar permissão
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            // Android 12 e inferior não precisa de permissão runtime
            DailyQuoteAlarmManager.scheduleDailyNotification(context)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = gradientColors,
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f)
                )
            )
    ) {
        quote?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Frase
                Text(
                    text = "\"${it.text}\"",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 36.sp
                    ),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Autor
                Text(
                    text = "— ${it.author}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    textAlign = TextAlign.End,
                    color = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 48.dp)
                )

                // Botões
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Botão Favoritar
                    FloatingActionButton(
                        onClick = { viewModel.toggleFavorite() },
                        containerColor = Color.White.copy(alpha = 0.9f),
                        contentColor = if (it.isFavorite) Color(0xFFE91E63) else Color(0xFF667eea),
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            imageVector = if (it.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favoritar",
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Botão Copiar
                    FloatingActionButton(
                        onClick = {
                            copyQuoteToClipboard(context, it)
                            Toast.makeText(context, "Frase copiada!", Toast.LENGTH_SHORT).show()
                        },
                        containerColor = Color.White.copy(alpha = 0.9f),
                        contentColor = Color(0xFF667eea),
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ContentCopy,
                            contentDescription = "Copiar",
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Botão Compartilhar
                    FloatingActionButton(
                        onClick = { shareQuote(context, it) },
                        containerColor = Color.White.copy(alpha = 0.9f),
                        contentColor = Color(0xFF667eea),
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = "Compartilhar",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                // Informação sobre nova frase
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = "Nova frase amanhã 🌅",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
        }
    }
}

private fun copyQuoteToClipboard(context: Context, quote: Quote) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(
        "quote",
        "\"${quote.text}\" — ${quote.author}"
    )
    clipboard.setPrimaryClip(clip)
}

private fun shareQuote(context: Context, quote: Quote) {
    val shareText = context.getString(R.string.share_template, quote.text, quote.author)
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, shareText)
        putExtra(Intent.EXTRA_SUBJECT, "Frase Motivacional")
    }
    context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_quote)))
}


