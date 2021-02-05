package com.highstreet.lib

import android.app.Application
import com.highstreet.lib.extensions.notNullSingleValue
import io.reactivex.plugins.RxJavaPlugins

/**
 * @author Yang Shihao
 * @Date 2/3/21
 */
open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        RxJavaPlugins.setErrorHandler { it.printStackTrace() }
    }

    companion object {
        var instance by notNullSingleValue<BaseApplication>()
    }
}