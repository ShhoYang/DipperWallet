package com.highstreet.wallet.cache

/**
 * @author Yang Shihao
 * @Date 3/11/21
 */
abstract class AbsCacheManager<T> {

    private var loading = false

    private val list = ArrayList<T>()

    private var cacheCallback: CacheCallback<T>? = null

    fun loadData() {
        if (loading) {
            return
        }
        loading = true
        doLoad()
    }

    fun getCache(cacheCallback: CacheCallback<T>) {
        if (list.isEmpty()) {
            this.cacheCallback = cacheCallback
            loadData()
        } else {
            cacheCallback.response(list)
        }
    }

    fun getSize() = list.size

    fun loaded(data: List<T>?) {
        if (data != null && data.isNotEmpty()) {
            list.clear()
            list.addAll(data)
            cacheCallback?.response(list)
        }
        loading = false
    }

    abstract fun doLoad()
}