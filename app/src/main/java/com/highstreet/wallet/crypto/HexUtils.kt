package com.highstreet.wallet.crypto

import java.lang.IllegalArgumentException
import java.math.BigInteger

object HexUtils {

    fun bytesToHexString(bytes: ByteArray): String {
        val hexArray = charArrayOf(
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
            'a',
            'b',
            'c',
            'd',
            'e',
            'f'
        )
        val hexChars = CharArray(bytes.size * 2)
        var v: Int
        for (i in bytes.indices) {
            v = bytes[i].toInt() and 0xFF
            hexChars[i * 2] = hexArray[v ushr 4]
            hexChars[i * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }

    fun hexStringToBytes(s: String): ByteArray {
        val len = s.length
        if (len % 2 == 1) {
            throw IllegalArgumentException("Hex string must have even number of characters")
        }
        val data = ByteArray(len / 2)
        for (i in 0 until len step 2) {
            data[i / 2] =
                ((Character.digit(s[i], 16) shl 4) + Character.digit(s[i + 1], 16)).toByte()
        }
        return data
    }

    fun integerToBytes(s: BigInteger, length: Int): ByteArray {
        val bytes = s.toByteArray()
        if (length < bytes.size) {
            val tmp = ByteArray(length)
            System.arraycopy(bytes, bytes.size - tmp.size, tmp, 0, tmp.size)
            return tmp
        } else if (length > bytes.size) {
            val tmp = ByteArray(length)
            System.arraycopy(bytes, 0, tmp, tmp.size - bytes.size, bytes.size)
            return tmp
        }
        return bytes
    }
}