package com.highstreet.wallet.utils

import androidx.annotation.StringRes
import com.highstreet.wallet.App

/**
 * @author Yang Shihao
 * @Date 2/3/21
 */
object ResourcesUtils {

    fun getString(@StringRes resId: Int): String {
        return  App.instance.getString(resId)
    }
}