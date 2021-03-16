package com.highstreet.wallet.cache

/**
 * @author Yang Shihao
 * @Date 3/16/21
 */
class CacheManager {

    fun load() {
        BalanceCache.instance().load()
        CoinPriceCache.instance().load()
    }

    companion object {
        private var instance: CacheManager? = null

        @Synchronized
        fun instance(): CacheManager {
            if (instance == null) {
                instance = CacheManager()
            }
            return instance!!
        }
    }
}