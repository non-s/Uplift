package com.motivacional.frases.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import com.motivacional.frases.data.model.Quote
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ShareHelper {

    fun shareQuoteAsText(context: Context, quote: Quote) {
        val shareText = "\"${quote.text}\"\n\n— ${quote.author}"

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }

        context.startActivity(
            Intent.createChooser(shareIntent, "Compartilhar frase")
        )
    }

    fun shareQuoteAsImage(context: Context, quote: Quote) {
        try {
            val imageGenerator = QuoteImageGenerator(context)
            val bitmap = imageGenerator.generateQuoteImage(quote)

            val cachePath = File(context.cacheDir, "images")
            cachePath.mkdirs()

            val file = File(cachePath, "quote_${System.currentTimeMillis()}.png")
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.close()

            val contentUri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, contentUri)
                type = "image/png"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(
                Intent.createChooser(shareIntent, "Compartilhar imagem")
            )

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
