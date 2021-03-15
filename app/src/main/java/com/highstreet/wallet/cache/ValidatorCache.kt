package com.highstreet.wallet.cache

import com.hao.library.http.subscribeBy
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.model.res.Validator

/**
 * @author Yang Shihao
 * @Date 3/11/21
 */
class ValidatorCache : AbsCacheManager<Validator>() {

    override fun doLoad() {
        ApiService.getApi().validators(1, Int.MAX_VALUE).subscribeBy({
            loaded(it)
        }, {
            loaded(null)
        })
    }

    companion object {

        private var instance: ValidatorCache? = null

        @Synchronized
        fun instance(): ValidatorCache {
            if (instance == null) {
                instance = ValidatorCache()
            }
            return instance!!
        }
    }
}