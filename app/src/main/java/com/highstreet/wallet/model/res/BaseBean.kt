package com.highstreet.wallet.model.res

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */
data class BaseBean<T>(
        var error: String?,
        var height: String?,
        var result: T?)