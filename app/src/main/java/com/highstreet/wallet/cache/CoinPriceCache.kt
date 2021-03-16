package com.highstreet.wallet.cache

import com.hao.library.http.subscribeBy2
import com.hao.library.utils.SPUtils
import com.highstreet.wallet.App
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.model.res.CoinPrice

/**
 * @author Yang Shihao
 * @Date 3/15/21
 */
class CoinPriceCache {

    private val map = HashMap<String, String>()

    fun getPrice(type: String): String {
        return map[type] ?: SPUtils.get(App.instance, type, "0.0")
    }

    fun load() {
        ApiService.getApi().price()
            .subscribeBy2({
                saveData(it)
            })
    }

    private fun saveData(coinPrice: CoinPrice?) {
        if (coinPrice == null || coinPrice.price.isEmpty()) {
            return
        }
        map.clear()
        coinPrice.price.forEach { (k, v) ->
            map[k] = v
            SPUtils.put(App.instance, k.toUpperCase(), v.toString())
        }
    }

    companion object {
        private var instance: CoinPriceCache? = null

        @Synchronized
        fun instance(): CoinPriceCache {
            if (instance == null) {
                instance = CoinPriceCache()
            }
            return instance!!
        }
    }
}