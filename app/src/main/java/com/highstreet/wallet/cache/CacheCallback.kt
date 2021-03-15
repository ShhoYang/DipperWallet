package com.highstreet.wallet.cache

/**
 * @author Yang Shihao
 * @Date 3/11/21
 */

interface CacheCallback<T> {

    fun response(data: ArrayList<T>)
}