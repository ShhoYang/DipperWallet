package com.highstreet.wallet.crypto

import android.security.keystore.KeyProperties
import org.bitcoinj.core.Sha256Hash
import java.security.MessageDigest

/**
 * @author Yang Shihao
 * @Date 1/19/21
 */
object SHA {

    fun hash(data: ByteArray): ByteArray {
        return try {
            val messageDigest = MessageDigest.getInstance(KeyProperties.DIGEST_SHA256)
            messageDigest.digest(data)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            byteArrayOf()
        }
    }

    fun createSha256Hash(data: ByteArray): Sha256Hash {
        return try {
            Sha256Hash.wrap(hash(data))
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Sha256Hash.wrap(byteArrayOf())
        }
    }
}