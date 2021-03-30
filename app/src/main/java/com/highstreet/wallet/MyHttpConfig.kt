package com.highstreet.wallet

import com.hao.library.HaoLibraryConfig
import com.hao.library.HttpConfig
import com.hao.library.http.HttpResponseModel

/**
 * @author Yang Shihao
 * @Date 3/8/21
 */
class MyHttpConfig : HttpConfig {


    override fun isLogin(): Boolean {
        return true
    }

    override fun login() {

    }

    override fun getBaseUrl(): String {
        return ""
    }

    override fun <T : HttpResponseModel<*>> handleResponse(t: T): Boolean {
        return false
    }
}