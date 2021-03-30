package com.highstreet.wallet.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import java.security.KeyStoreException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec

/**
 * @author Yang Shihao
 * @Date 1/22/21
 *
 * 对称加密
 */
object AES {

    private const val TRANSFORMATION = "AES/GCM/NoPadding"

    /**
     * 加密
     */
    fun encrypt(
        resource: String,
        keystoreAlias: String,
        auth: Boolean = false
    ): Pair<String, String>? {
        return try {
            val keyStore = KeyStoreUtils.loadKeyStore()
            generateKeyIfNecessary(keyStore, keystoreAlias, auth)
            val key = keyStore.getKey(keystoreAlias, null)
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val end = cipher.doFinal(resource.toByteArray(Charsets.UTF_8))
            Pair(Base64Utils.encode(end), Base64Utils.encode(cipher.iv))
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 解密
     */
    fun decrypt(resource: String, iv: String, keystoreAlias: String): String? {
        return try {
            val keyStore = KeyStoreUtils.loadKeyStore()
            val secretKey =
                (keyStore.getEntry(keystoreAlias, null) as KeyStore.SecretKeyEntry).secretKey
            val spec = GCMParameterSpec(128, Base64Utils.decode(iv))
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
            String(cipher.doFinal(Base64Utils.decode(resource)), Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 判断别名是否存在，如果不存在则创建
     */
    private fun generateKeyIfNecessary(
        keyStore: KeyStore,
        keystoreAlias: String,
        auth: Boolean
    ): Boolean {
        return try {
            keyStore.containsAlias(keystoreAlias) || generateKey(keystoreAlias, auth)
        } catch (e: KeyStoreException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 生成新的秘钥
     */
    private fun generateKey(keystoreAlias: String, auth: Boolean): Boolean {
        return try {
            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                KeyStoreUtils.KEYSTORE
            )
            keyGenerator.init(
                KeyGenParameterSpec.Builder(
                    keystoreAlias,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setUserAuthenticationRequired(auth)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build()
            )
            keyGenerator.generateKey()
            true
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            false
        }
    }
}