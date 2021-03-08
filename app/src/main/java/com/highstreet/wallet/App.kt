package com.highstreet.wallet

import androidx.multidex.MultiDexApplication
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.hao.library.HaoLibrary
import com.hao.library.HaoLibraryConfig
import com.hao.library.extensions.notNullSingleValue
import com.hao.library.http.HttpResponseModel
import com.hao.library.service.InitX5Service
import com.hao.library.utils.AppUtils
import com.hao.library.utils.CoroutineUtils
import com.highstreet.wallet.backup.BaseData
import com.highstreet.wallet.ui.activity.CrashActivity
import com.highstreet.wallet.ui.activity.WelcomeActivity
import com.tencent.bugly.crashreport.CrashReport

class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        HaoLibrary.init(this, LibraryConfig())
        if (!BuildConfig.testnet) {
            CrashReport.initCrashReport(applicationContext, "88dfb47f91", false)
        }
        if (AppUtils.isMainProcess(instance, android.os.Process.myPid())) {
            CoroutineUtils.io {
                InitX5Service.start(instance)
            }
        }
        CaocConfig.Builder
            .create()
            .errorActivity(CrashActivity::class.java)
            .restartActivity(WelcomeActivity::class.java)
            .apply()
    }

    private var baseData: BaseData? = null

    fun getOldDB(): BaseData {
        if (baseData == null) {
            baseData = BaseData(this)
        }
        return baseData!!
    }

    companion object {
        open var instance by notNullSingleValue<App>()
    }
}

// orient, gloom, about, better,
// trigger, beyond, visual, merit,
// best, first, broom, opera,
// color, boost, labor, piece,
// jar, renew, cannon, horn,
// slot, alone, stove, use