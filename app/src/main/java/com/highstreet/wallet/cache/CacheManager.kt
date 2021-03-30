package com.highstreet.wallet.cache

/**
 * @author Yang Shihao
 * @Date 3/16/21
 */
object CacheManager {

    fun load() {
        BalanceCache.instance().load()
        CoinPriceCache.instance().load()
    }
}