package com.highstreet.wallet

import androidx.multidex.MultiDexApplication
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.hao.library.HaoLibrary
import com.hao.library.HaoLibraryConfig
import com.hao.library.extensions.notNullSingleValue
import com.hao.library.http.HttpResponseModel
import com.hao.library.view.EmptyView
import com.highstreet.wallet.backup.BaseData
import com.highstreet.wallet.ui.activity.CrashActivity
import com.highstreet.wallet.ui.activity.WelcomeActivity
import com.tencent.bugly.crashreport.CrashReport

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        HaoLibrary.init(this, haoLibraryConfig)
        if (!BuildConfig.testnet) {
            CrashReport.initCrashReport(applicationContext, "88dfb47f91", false)
            CaocConfig.Builder
                .create()
                .errorActivity(CrashActivity::class.java)
                .restartActivity(WelcomeActivity::class.java)
                .apply()
        }

        EmptyView
    }

    private var baseData: BaseData? = null

    fun getOldDB(): BaseData {
        if (baseData == null) {
            baseData = BaseData(this)
        }
        return baseData!!
    }

    private val haoLibraryConfig by lazy {
        object : HaoLibraryConfig() {

            override fun toolbarLayoutTheme(): Int {
                return R.style.AppToolbarLayout
            }

            override fun isLogin(): Boolean {
                return true
            }

            override fun login() {

            }

            override fun getBaseUrl(): String {
                return BuildConfig.BASE_URL
            }

            override fun <T : HttpResponseModel<*>> handleResponse(t: T): Boolean {
                return false
            }
        }
    }

    companion object {
        open var instance by notNullSingleValue<App>()
    }
}