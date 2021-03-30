package com.highstreet.wallet

import android.app.Application
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.hao.library.HaoLibrary
import com.hao.library.HaoLibraryConfig
import com.hao.library.extensions.notNullSingleValue
import com.hao.library.utils.L
import com.highstreet.wallet.backup.BaseData
import com.highstreet.wallet.cache.CacheManager
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

class App : Application() {

    private var baseData: BaseData? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        HaoLibrary.init(libraryConfig())
        CrashReport.initCrashReport(applicationContext, "88dfb47f91", false)
        initX5()
        CacheManager.load()
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

    private fun libraryConfig(): HaoLibraryConfig {
        return HaoLibraryConfig.Builder(this)
            .setToolbarLayoutTheme(R.style.AppToolbarLayout)
            .setEmptyViewTheme(R.style.AppEmptyView)
            .setConfirmDialogTheme(R.style.AppConfirmDialog)
            .setLoadingDialogTheme(R.style.AppLoadingDialog)
            .setHttpConfig(MyHttpConfig())
            .build()
    }

    companion object {
        private const val TAG = "--App--"
        var instance by notNullSingleValue<App>()
    }
}