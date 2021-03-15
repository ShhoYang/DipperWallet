package com.highstreet.wallet

import androidx.multidex.MultiDexApplication
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.hao.library.HaoLibrary
import com.hao.library.extensions.notNullSingleValue
import com.hao.library.utils.L
import com.highstreet.wallet.backup.BaseData
import com.highstreet.wallet.ui.activity.CrashActivity
import com.highstreet.wallet.ui.activity.WelcomeActivity
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.smtt.sdk.QbSdk


/**
 * todo
 *
 * 1》UI
 * 2》dapp
 * 3》合约转账
 * 4》tokens
 */

class App : MultiDexApplication() {

    private var baseData: BaseData? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        HaoLibrary.init(this, LibraryConfig())
        if (!BuildConfig.testnet) {
            CrashReport.initCrashReport(applicationContext, "88dfb47f91", false)
        }
        initX5()
        CaocConfig.Builder
            .create()
            .errorActivity(CrashActivity::class.java)
            .restartActivity(WelcomeActivity::class.java)
            .apply()
    }

    fun getOldDB(): BaseData {
        if (baseData == null) {
            baseData = BaseData(this)
        }
        return baseData!!
    }

    private fun initX5() {
        val callback = object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {
                L.d(TAG, "onCoreInitFinished")
            }

            override fun onViewInitFinished(p0: Boolean) {
                L.d(TAG, "onViewInitFinished:$p0")
            }
        }
        QbSdk.setDownloadWithoutWifi(true)
        QbSdk.initX5Environment(this, callback)
    }

    companion object {
        private const val TAG = "--App--"
        var instance by notNullSingleValue<App>()
    }
}

// orient, gloom, about, better,
// trigger, beyond, visual, merit,
// best, first, broom, opera,
// color, boost, labor, piece,
// jar, renew, cannon, horn,
// slot, alone, stove, use