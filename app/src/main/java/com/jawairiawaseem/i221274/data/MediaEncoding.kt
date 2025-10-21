package com.jawairiawaseem.i221274.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

object MediaEncoding {

    // Compress image bytes -> JPEG -> Base64 (quality ~70 to keep light)
    fun imageBytesToBase64(imageBytes: ByteArray, jpegQuality: Int = 70): String {
        val bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        val out = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, jpegQuality.coerceIn(40, 90), out)
        return Base64.encodeToString(out.toByteArray(), Base64.DEFAULT)
    }

    fun bitmapFromBase64(base64: String): Bitmap? {
        val bytes = Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    // Video: read all bytes -> Base64 (keep videos short!)
    fun videoStreamToBase64(input: InputStream): String {
        val bytes = input.readBytes()
        input.close()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    // Write Base64 video to a temp file and return its Uri for playback
    fun base64ToTempVideoUri(context: Context, base64: String, ext: String = ".mp4"): Uri {
        val bytes = Base64.decode(base64, Base64.DEFAULT)
        val f = File.createTempFile("story_", ext, context.cacheDir)
        f.writeBytes(bytes)
        return Uri.fromFile(f)
    }

    // Helper to read all bytes from content Uri
    fun readAllBytes(context: Context, uri: Uri): ByteArray {
        context.contentResolver.openInputStream(uri).use { ins ->
            return ins?.readBytes() ?: ByteArray(0)
        }
    }
}
