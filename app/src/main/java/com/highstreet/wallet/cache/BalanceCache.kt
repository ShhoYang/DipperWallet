package com.highstreet.wallet.cache

import com.hao.library.http.subscribeBy3
import com.hao.library.utils.L
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.constant.Chain
import com.highstreet.wallet.db.Account
import com.highstreet.wallet.db.Token
import com.highstreet.wallet.http.ApiService
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author Yang Shihao
 * @Date 3/15/21
 */
class BalanceCache {

    private val map = HashMap<String, String>()

    fun getBalance(account: Account): String {
        return map[account.chain + account.address] ?: "--"
    }

    fun getBalance(token: Token): String {
        return map[token.chain + token.address] ?: "--"
    }

    fun load() {
        Observable.create(ObservableOnSubscribe<Pair<String, String>> { e ->
            val accounts = AccountManager.instance().accounts
            accounts.forEach { account ->
                ApiService.getApi(Chain.getChain(account.chain)).account(account.address)
                    .subscribeBy3({
                        e.onNext(
                            Pair(
                                account.chain + account.address,
                                it?.getAmount() ?: "--"
                            ),
                        )
                    },{})
            }
            e.onComplete()
        }).subscribeOn(
            Schedulers.io()
        ).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                map[it.first] = it.second
                L.d(TAG, "next ${it.first}--${it.second}")
            }, {
                L.d(TAG, "error")
            }, {
                L.d(TAG, "completed")
            })
    }

    companion object {

        private const val TAG = "--BalanceCache--"

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