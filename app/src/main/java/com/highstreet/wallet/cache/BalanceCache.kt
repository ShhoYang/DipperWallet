package com.highstreet.wallet.cache

import com.hao.library.utils.CoroutineUtils
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.constant.Chain
import com.highstreet.wallet.db.Account
import com.highstreet.wallet.http.ApiService
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Yang Shihao
 * @Date 3/15/21
 */
class BalanceCache {

    private val map = ConcurrentHashMap<String, String>()

    fun getBalance(account: Account): String {
        return map[account.chain + account.address] ?: "--"
    }

    fun load() {
        CoroutineUtils.io {
            val accounts = AccountManager.instance().accounts
            accounts.forEach { account ->
                ApiService.getApi(Chain.getChain(account.chain)).account(account.address)
                    .subscribe({
                        map[account.chain + account.address] = it?.result?.getAmount() ?: "--"
                    }, {

                    })
            }
        }
    }

    companion object {

        private var instance: BalanceCache? = null

        @Synchronized
        fun instance(): BalanceCache {
            if (instance == null) {
                instance = BalanceCache()
            }
            return instance!!
        }
    }
}