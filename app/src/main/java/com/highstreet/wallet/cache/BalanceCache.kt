package com.highstreet.wallet.cache

import android.util.ArrayMap
import com.hao.library.utils.CoroutineUtils
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.constant.Chain
import com.highstreet.wallet.db.Account
import com.highstreet.wallet.http.ApiService

/**
 * @author Yang Shihao
 * @Date 3/15/21
 */
class BalanceCache {

    private val map = ArrayMap<String, String>()

    fun getBalance(account: Account): String {
        return map[account.chain + account.address] ?: "--"
    }

    fun loadBalances() {
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