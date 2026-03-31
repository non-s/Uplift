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
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
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
import com.motivacional.frases.ui.viewmodel.ThemeViewModel
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: QuoteViewModel = viewModel(),
    themeViewModel: ThemeViewModel = viewModel()
) {
    val context = LocalContext.current
    val quote by viewModel.quote.collectAsStateWithLifecycle()
    val themeColor by themeViewModel.themeColor.collectAsStateWithLifecycle()
    var isSwiping by remember { mutableStateOf(false) }

    // Solicitar permissão de notificação para Android 13+
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            DailyQuoteAlarmManager.scheduleDailyNotification(context)
        }
    }

    // Cores de gradiente baseadas no tema selecionado
    val gradientColors = when (themeColor) {
        "purple" -> listOf(Color(0xFF667eea), Color(0xFF764ba2))
        "green" -> listOf(Color(0xFF11998e), Color(0xFF38ef7d))
        "orange" -> listOf(Color(0xFFf2994a), Color(0xFFf2c94c))
        "pink" -> listOf(Color(0xFFec008c), Color(0xFFfc6767))
        "red" -> listOf(Color(0xFFe52d27), Color(0xFFb31217))
        "blue" -> listOf(Color(0xFF2193b0), Color(0xFF6dd5ed))
        else -> listOf(Color(0xFF667eea), Color(0xFF764ba2), Color(0xFFf093fb), Color(0xFF4facfe))
    }

    LaunchedEffect(key1 = true) {
        viewModel.loadDailyQuote()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                DailyQuoteAlarmManager.scheduleDailyNotification(context)
            } else {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            DailyQuoteAlarmManager.scheduleDailyNotification(context)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Uplift",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        ),
                        color = Color.White
                    )
                },
                actions = {
                    IconButton(onClick = { context.startActivity(Intent(context, SearchActivity::class.java)) }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.White)
                    }
                    IconButton(onClick = { context.startActivity(Intent(context, SettingsActivity::class.java)) }) {
                        Icon(Icons.Default.Settings, contentDescription = "Configurações", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
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
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = { isSwiping = true },
                        onDragEnd = { isSwiping = false },
                        onHorizontalDrag = { change, dragAmount ->
                            if (dragAmount < -50 && !isSwiping) {
                                viewModel.loadRandomQuote()
                                isSwiping = true
                            }
                            if (dragAmount > 50 && !isSwiping) {
                                viewModel.loadRandomQuote()
                                isSwiping = true
                            }
                        }
                    )
                }
        ) {
            AnimatedContent(
                targetState = quote,
                transitionSpec = {
                    if (targetState != initialState) {
                        (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                            slideOutHorizontally { width -> -width } + fadeOut())
                    } else {
                        fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
                    }
                },
                label = "quote_transition"
            ) { targetQuote ->
                targetQuote?.let {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Card Glassmorphism
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            shape = RoundedCornerShape(32.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.15f)
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "\"${it.text}\"",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontSize = 26.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        lineHeight = 38.sp
                                    ),
                                    textAlign = TextAlign.Center,
                                    color = Color.White
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                Text(
                                    text = "— ${it.author}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Light
                                    ),
                                    color = Color.White.copy(alpha = 0.8f),
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(48.dp))

                        // Botões de Ação
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ActionButton(
                                icon = if (it.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favoritar",
                                tint = if (it.isFavorite) Color(0xFFF44336) else Color.White,
                                onClick = { viewModel.toggleFavorite() }
                            )

                            ActionButton(
                                icon = Icons.Outlined.ContentCopy,
                                contentDescription = "Copiar",
                                onClick = {
                                    copyQuoteToClipboard(context, it)
                                    Toast.makeText(context, "Copiado para o clipboard!", Toast.LENGTH_SHORT).show()
                                }
                            )

                            ActionButton(
                                icon = Icons.Outlined.Share,
                                contentDescription = "Compartilhar",
                                onClick = { shareQuote(context, it) }
                            )
                        }

                        Spacer(modifier = Modifier.height(64.dp))

                        Text(
                            text = "Arraste para o lado por mais inspiração ✨",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.5f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    tint: Color = Color.White,
    onClick: () -> Unit
) {
    FilledIconButton(
        onClick = onClick,
        modifier = Modifier.size(64.dp),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = Color.White.copy(alpha = 0.2f),
            contentColor = tint
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(28.dp)
        )
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


