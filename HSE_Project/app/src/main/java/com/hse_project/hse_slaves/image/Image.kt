package com.hse_project.hse_slaves.image

import android.app.Activity
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

/*
 * Чтобы добавить любое изображение (получим String)
 * val str = Base64.encodeToString(getBytes(contentResolver.openInputStream(URI)), Base64.DEFAULT)
 * Чтобы обратно его получить (получим Bitmap)
 * val bmp = Base64.decode(str, Base64.DEFAULT)
 * val bmp1 = BitmapFactory.decodeByteArray(bmp, 0, bmp.size)
 */

@Throws(IOException::class)
private fun getBytes(inputStream: InputStream?): ByteArray? {
    val byteBuffer = ByteArrayOutputStream()
    val bufferSize = 1024
    val buffer = ByteArray(bufferSize)
    var len: Int
    while (inputStream!!.read(buffer).also { len = it } != -1) {
        byteBuffer.write(buffer, 0, len)
    }
    return byteBuffer.toByteArray()
}

/**
 * Переводит изображение по uri в массив байт, а затем в строку.
 * Возвращает строку закодированную с помощью Base64
 *
 */
fun getStringByUri(uri: Uri, activity: Activity): String? {
    return Base64.encodeToString(
        getBytes(
            ContextWrapper(activity).contentResolver.openInputStream(
                uri
            )
        ), Base64.DEFAULT
    )
}

/**
 * Конвертирует строку из байтов, закодированную с помощью Base64,
 * в Bitmap
 */
fun getBitmapByString(str: String): Bitmap {
    val bmp = Base64.decode(str, Base64.DEFAULT)
    val bmp1 = BitmapFactory.decodeByteArray(bmp, 0, bmp.size)
    val width: Int = bmp1.width * 512 / bmp1.height
    return Bitmap.createScaledBitmap(bmp1, width, 512, true)
}
