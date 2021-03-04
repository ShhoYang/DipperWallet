package com.highstreet.wallet.model.res

import com.hao.library.http.HttpResponseModel

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */
class BaseBean<T>(
    var error: String?,
    var height: String?,
    var result: T?
) : HttpResponseModel<T> {
    override fun getCode(): String {
        return error ?: ""
    }

    override fun getData(): T? {
        return result
    }

    override fun getMessage(): String {
        return error ?: ""
    }

    override fun isSucceed(): Boolean {
        return true
    }
}