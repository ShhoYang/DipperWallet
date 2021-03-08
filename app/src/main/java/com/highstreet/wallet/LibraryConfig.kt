package com.highstreet.wallet

import com.hao.library.HaoLibraryConfig
import com.hao.library.http.HttpResponseModel

/**
 * @author Yang Shihao
 * @Date 3/8/21
 */
class LibraryConfig : HaoLibraryConfig() {
    override fun toolbarLayoutTheme(): Int {
        return R.style.AppToolbarLayout
    }

    override fun emptyViewTheme(): Int {
        return R.style.AppEmptyView
    }

    override fun confirmDialogTheme(): Int {
        return R.style.AppConfirmDialog
    }

    override fun loadingDialogTheme(): Int {
        return R.style.AppLoadingDialog
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