package com.highstreet.wallet.fingerprint.listener

/**
 * @author Yang Shihao
 * @Date 2020/10/20
 */
interface FingerprintCallback {
    /**
     * 验证成功
     */
    fun onFingerprintAuthenticateSucceed()

    fun onFingerprintCancel()

    fun usePassword(password: String): Boolean?
}