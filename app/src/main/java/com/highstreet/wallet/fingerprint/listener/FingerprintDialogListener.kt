package com.highstreet.wallet.fingerprint.listener

/**
 * @author Yang Shihao
 * @Date 2020/10/21
 */

interface FingerprintDialogListener {

    fun cancelFingerprint()

    fun usePassword(password: String): Boolean?
}