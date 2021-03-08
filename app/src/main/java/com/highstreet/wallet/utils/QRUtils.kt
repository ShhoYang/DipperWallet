package com.highstreet.wallet.utils

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

/**
 * @author Yang Shihao
 * @Date 3/6/21
 */
object QRUtils {

    fun generateQRCode(content: String, width: Int, height: Int): Bitmap? {
        val qrCodeWriter = QRCodeWriter()
        val hints: MutableMap<EncodeHintType, String?> = HashMap()
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
        try {
            val encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints)
            val pixels = IntArray(width * height)
            for (i in 0 until height) {
                for (j in 0 until width) {
                    if (encode[j, i]) {
                        pixels[i * width + j] = 0x000000
                    } else {
                        pixels[i * width + j] = 0XFFFFFF
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }
}