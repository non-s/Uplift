package com.motivacional.frases.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
fun getTypography(fontSize: String): Typography {
    val baseBodyLargeSize = 16.sp
    val baseLabelMediumSize = 12.sp

    val (bodyLargeSize, labelMediumSize) = when (fontSize) {
        "small" -> Pair(baseBodyLargeSize * 0.8f, baseLabelMediumSize * 0.8f)
        "large" -> Pair(baseBodyLargeSize * 1.2f, baseLabelMediumSize * 1.2f)
        "xlarge" -> Pair(baseBodyLargeSize * 1.4f, baseLabelMediumSize * 1.4f)
        else -> Pair(baseBodyLargeSize, baseLabelMediumSize) // "medium" or default
    }

    return Typography(
        bodyLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = bodyLargeSize,
            lineHeight = bodyLargeSize * 1.5f,
            letterSpacing = 0.5.sp
        ),
        labelMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = labelMediumSize,
            lineHeight = labelMediumSize * 1.3f,
            letterSpacing = 0.5.sp
        )
    )
}