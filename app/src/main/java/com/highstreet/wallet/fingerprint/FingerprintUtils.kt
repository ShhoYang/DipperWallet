package com.highstreet.wallet.fingerprint

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.fingerprint.listener.FingerprintCallback

/**
 * @author Yang Shihao
 * @Date 2020/10/20
 */
object FingerprintUtils {

    /**
     * 指纹是否可用
     */
    fun isAvailable(context: Context?): Boolean {

        if (null == context) {
            return false
        }

        return isHardwareDetected(context) && hasEnrolledFingerprints(context)
    }

    /**
     * 设备是否支持指纹
     */
    fun isHardwareDetected(context: Context?): Boolean {

        if (null == context) {
            return false
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false
        }
        return FingerprintManagerCompat.from(context).isHardwareDetected
    }

    /**
     * 是否录入指纹
     */
    fun hasEnrolledFingerprints(context: Context?): Boolean {

        if (null == context) {
            return false
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false
        }
        return FingerprintManagerCompat.from(context).hasEnrolledFingerprints()
    }

    fun getFingerprint(
        activity: Activity,
        useFingerprint: Boolean?,
        showUserPassword: Boolean,
        succeed: () -> Unit,
        cancel: (() -> Unit)? = null
    ): IFingerprint {
        val fingerprint = FingerprintM()
        val useFingerprint2 =
            useFingerprint ?: (isAvailable(activity) && AccountManager.instance().fingerprint)
        fingerprint.init(
            activity,
            useFingerprint2,
            object : FingerprintCallback {
                override fun onFingerprintAuthenticateSucceed() {
                    succeed()
                }

                override fun onFingerprintCancel() {
                    if (cancel != null) {
                        cancel()
                    }
                }

                override fun usePassword(password: String): Boolean {
                    if (showUserPassword && AccountManager.instance().password!!.verify(password)) {
                        succeed()
                        return true
                    }
                    return false
                }

            },
            DialogParams(useFingerprint = useFingerprint2, showUserPassword = showUserPassword)
        )

        return fingerprint
    }
}