package com.highstreet.wallet.ui.vm

import androidx.lifecycle.MutableLiveData
import com.hao.library.http.subscribeBy
import com.hao.library.http.subscribeBy2
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.App
import com.highstreet.wallet.R
import com.highstreet.wallet.utils.AmountUtils
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.model.req.Coin
import com.highstreet.wallet.model.req.RequestBroadCast
import com.highstreet.wallet.model.res.AccountInfo
import com.highstreet.wallet.crypto.KeyUtils
import com.highstreet.wallet.utils.MsgGeneratorUtils

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */

class TransactionVM : BalanceVM() {

    val resultLD = MutableLiveData<Pair<Boolean, String>>()

    fun transact(
        toAddress: String,
        toAmount: String,
        allAmount: Long,
        isAll: Boolean,
        remarks: String
    ) {
        ApiService.getDipApi().account(AccountManager.instance().address).subscribeBy({
            if (isAll) {
                generateParams(
                    it,
                    toAddress,
                    AmountUtils.generateCoin(allAmount, true),
                    remarks
                )
            } else {
                val coins = it?.value?.coins
                if (null != coins && coins.isNotEmpty()) {
                    val balance = coins[0].amount ?: "0"
                    if (!AmountUtils.isEnough(balance, toAmount)) {
                        resultLD.value =
                            Pair(false, App.instance.getString(R.string.notEnoughToTransfer))
                        return@subscribeBy
                    }
                }
                generateParams(
                    it,
                    toAddress,
                    AmountUtils.generateCoin(toAmount),
                    remarks
                )
            }
        }, {
            resultLD.value = Pair(false, App.instance.getString(R.string.failed))
        }).add()
    }

    private fun generateParams(
        accountInfo: AccountInfo?,
        toAddress: String,
        coin: Coin,
        remarks: String
    ) {
        if (accountInfo == null) {
            resultLD.value = Pair(false, App.instance.getString(R.string.failed))
            return
        }
        val account = AccountManager.instance().account!!
        account.accountNumber = accountInfo.getAccountNumber()
        account.sequenceNumber = accountInfo.getSequence()
        val coinList = ArrayList<Coin>()
        coinList.add(coin)
        val deterministicKey =
            KeyUtils.getDeterministicKey(account.chain, account.getEntropyAsHex(), account.path)
        val msg = MsgGeneratorUtils.sendMsg(
            account.address,
            toAddress,
            coinList,
            account.chain
        )
        doTransact(
            MsgGeneratorUtils.getBroadCast(
                account,
                msg,
                AmountUtils.generateFee(),
                remarks,
                deterministicKey
            )
        )
    }

    private fun doTransact(reqBroadCast: RequestBroadCast) {
        ApiService.getDipApi().txs(reqBroadCast).subscribeBy2({
            if (true == it?.success()) {
                AccountManager.instance().refresh()
                resultLD.value = Pair(true, App.instance.getString(R.string.succeed))
            } else {
                resultLD.value = Pair(false, App.instance.getString(R.string.failed))
            }

        }, {
            resultLD.value = Pair(false, it.second)
        }).add()
    }
}