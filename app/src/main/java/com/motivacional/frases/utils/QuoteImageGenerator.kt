package com.motivacional.frases.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import com.motivacional.frases.R
import com.motivacional.frases.data.model.Quote

class QuoteImageGenerator(private val context: Context) {

    fun generateQuoteImage(quote: Quote): Bitmap {
        val width = 1080
        val height = 1920
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Gradiente de fundo baseado na categoria
        val gradient = getGradientForCategory(quote.category)
        canvas.drawPaint(Paint().apply {
            shader = LinearGradient(
                0f, 0f, 0f, height.toFloat(),
                gradient.first, gradient.second,
                Shader.TileMode.CLAMP
            )
        })

        // Configuração de margem e padding
        val padding = 120f
        val maxTextWidth = width - (padding * 2)

        // Desenhar texto da frase
        val quotePaint = Paint().apply {
            color = Color.WHITE
            textSize = 72f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        // Quebrar texto em múltiplas linhas
        val quoteText = "\"${quote.text}\""
        val quoteLines = breakTextIntoLines(quoteText, quotePaint, maxTextWidth)

        // Desenhar as linhas da frase
        var currentY = height / 2f - (quoteLines.size * 90f / 2f)
        quoteLines.forEach { line ->
            canvas.drawText(line, width / 2f, currentY, quotePaint)
            currentY += 90f
        }

        // Desenhar autor
        val authorPaint = Paint().apply {
            color = Color.WHITE
            alpha = 220
            textSize = 48f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        currentY += 60f
        canvas.drawText("— ${quote.author}", width / 2f, currentY, authorPaint)

        // Logo/marca d'água no rodapé
        val footerPaint = Paint().apply {
            color = Color.WHITE
            alpha = 150
            textSize = 36f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        canvas.drawText("Frases Motivacionais", width / 2f, height - 100f, footerPaint)

        return bitmap
    }

    private fun breakTextIntoLines(text: String, paint: Paint, maxWidth: Float): List<String> {
        val lines = mutableListOf<String>()
        val words = text.split(" ")
        var currentLine = ""

        for (word in words) {
            val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
            val testWidth = paint.measureText(testLine)

            if (testWidth > maxWidth && currentLine.isNotEmpty()) {
                lines.add(currentLine)
                currentLine = word
            } else {
                currentLine = testLine
            }
        }

        if (currentLine.isNotEmpty()) {
            lines.add(currentLine)
        }

        return lines
    }

    private fun getGradientForCategory(category: String): Pair<Int, Int> {
        return when (category) {
            "motivation" -> Pair(
                Color.parseColor("#667eea"),
                Color.parseColor("#764ba2")
            )
            "success" -> Pair(
                Color.parseColor("#f093fb"),
                Color.parseColor("#f5576c")
            )
            "love" -> Pair(
                Color.parseColor("#fa709a"),
                Color.parseColor("#fee140")
            )
            "life" -> Pair(
                Color.parseColor("#30cfd0"),
                Color.parseColor("#330867")
            )
            "wisdom" -> Pair(
                Color.parseColor("#a8edea"),
                Color.parseColor("#fed6e3")
            )
            "happiness" -> Pair(
                Color.parseColor("#ffecd2"),
                Color.parseColor("#fcb69f")
            )
            "inspiration" -> Pair(
                Color.parseColor("#ff9a9e"),
                Color.parseColor("#fecfef")
            )
            else -> Pair(
                Color.parseColor("#4facfe"),
                Color.parseColor("#00f2fe")
            )
        }
    }
}
