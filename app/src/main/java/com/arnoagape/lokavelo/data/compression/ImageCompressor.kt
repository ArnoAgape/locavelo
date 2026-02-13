package com.arnoagape.lokavelo.data.compression

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import androidx.core.graphics.scale

class ImageCompressor @Inject constructor(
    @param: ApplicationContext private val context: Context
) {

    suspend fun compress(uri: Uri): File = withContext(Dispatchers.IO) {

        // 1️⃣ Ouvrir le flux proprement (compatible content://)
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalStateException("Cannot open input stream")

        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()

        // 2️⃣ Redimensionner (max 1280px largeur)
        val maxWidth = 1280

        val resizedBitmap =
            if (originalBitmap.width > maxWidth) {
                val ratio = maxWidth.toFloat() / originalBitmap.width
                val height = (originalBitmap.height * ratio).toInt()
                originalBitmap.scale(maxWidth, height)
            } else {
                originalBitmap
            }


        // 3️⃣ Créer fichier temporaire
        val tempFile = File.createTempFile(
            "compressed_",
            ".jpg",
            context.cacheDir
        )

        val outputStream = FileOutputStream(tempFile)

        resizedBitmap.compress(
            Bitmap.CompressFormat.JPEG,
            80, // qualité optimale
            outputStream
        )

        outputStream.flush()
        outputStream.close()

        tempFile
    }
}