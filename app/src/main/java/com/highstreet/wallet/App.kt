package com.highstreet.wallet

import android.app.Application
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.highstreet.lib.extensions.notNullSingleValue
import com.highstreet.wallet.ui.activity.CrashActivity
import com.highstreet.wallet.ui.activity.WelcomeActivity
import com.tencent.bugly.crashreport.CrashReport
import io.reactivex.plugins.RxJavaPlugins

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        if(!BuildConfig.testnet){
            CrashReport.initCrashReport(applicationContext, "88dfb47f91", false)
            CaocConfig.Builder
                .create()
                .errorActivity(CrashActivity::class.java)
                .restartActivity(WelcomeActivity::class.java)
                .apply()
        }
        RxJavaPlugins.setErrorHandler { it.printStackTrace() }
    }

    companion object {
        var instance by notNullSingleValue<App>()
    }
}