package com.highstreet.wallet.crypto

import android.util.Base64

/**
 * @author Yang Shihao
 * @Date 1/19/21
 */
object Base64Utils {

    fun decode(content: String): ByteArray {
        return Base64.decode(content, Base64.DEFAULT)
    }

    fun encode(data: ByteArray): String {
        return Base64.encodeToString(data, Base64.DEFAULT)
    }
}