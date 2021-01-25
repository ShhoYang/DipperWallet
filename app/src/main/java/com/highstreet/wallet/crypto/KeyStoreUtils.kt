package com.highstreet.wallet.crypto

import java.security.KeyStore

/**
 * @author Yang Shihao
 * @Date 1/22/21
 */
object KeyStoreUtils {

    const val KEYSTORE = "AndroidKeyStore"

    @Throws(Exception::class)
    fun loadKeyStore(): KeyStore {
        val keyStore = KeyStore.getInstance(KEYSTORE)
        keyStore.load(null)
        return keyStore
    }
}