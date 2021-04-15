package com.highstreet.wallet.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.Signature

/**
 * @author Yang Shihao
 * @Date 1/22/21
 *
 * 非对称加密
 */
object RSA {

    private const val ALGORITHM = "SHA256withRSA"

    /**
     * 签名
     */
    fun sign(data: String, keystoreAlias: String): String? {
        return try {
            val keyStore = KeyStoreUtils.loadKeyStore()
            generateKeyPairIfNecessary(keyStore, keystoreAlias)
            val entry =
                (keyStore.getEntry(keystoreAlias, null) ?: return null) as? KeyStore.PrivateKeyEntry
                    ?: return null
            val signature = Signature.getInstance(ALGORITHM)
            signature.initSign(entry.privateKey)
            signature.update(data.toByteArray())
            Base64Utils.encode(signature.sign())
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * 验证
     */
    fun verify(input: String, signatureData: String, keystoreAlias: String): Boolean {
        return try {
            val keyStore = KeyStoreUtils.loadKeyStore()
            val signature = Signature.getInstance(ALGORITHM)
            signature.initVerify(keyStore.getCertificate(keystoreAlias))
            signature.update(input.toByteArray())
            signature.verify(Base64Utils.decode(signatureData))
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     *判断别名是否存在，如果不存在则创建
     */
    private fun generateKeyPairIfNecessary(keyStore: KeyStore, keystoreAlias: String): Boolean {
        return try {
            keyStore.containsAlias(keystoreAlias) || generateKeyPair(keystoreAlias)
        } catch (e: KeyStoreException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 生成新的秘钥对，包含一个公钥一个私钥
     */
    private fun generateKeyPair(keystoreAlias: String): Boolean {
        return try {
            val kpGenerator = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_RSA,
                KeyStoreUtils.KEYSTORE
            )
            kpGenerator.initialize(
                KeyGenParameterSpec.Builder(keystoreAlias, KeyProperties.PURPOSE_SIGN)
                    .setDigests(KeyProperties.DIGEST_SHA256)
                    .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                    .build()
            )
            kpGenerator.generateKeyPair()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}