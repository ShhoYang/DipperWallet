package com.highstreet.wallet.ui.vm

import androidx.lifecycle.MutableLiveData
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.R
import com.highstreet.wallet.utils.AmountUtils
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.http.subscribeBy
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
                    it.result!!,
                    toAddress,
                    AmountUtils.generateCoin(allAmount, true),
                    remarks
                )
            } else {
                val coins = it.result?.value?.coins
                if (null != coins && coins.isNotEmpty()) {
                    val balance = coins[0].amount ?: "0"
                    if (AmountUtils.isEnough(balance, toAmount)) {
                        generateParams(
                            it.result!!,
                            toAddress,
                            AmountUtils.generateCoin(toAmount),
                            remarks
                        )
                    } else {
                        resultLD.value =
                            Pair(false, getString(R.string.notEnoughToTransfer))
                    }
                }
            }
        }, {
            resultLD.value = Pair(false, getString(R.string.failed))
        }).add()
    }

    private fun generateParams(
        accountInfo: AccountInfo,
        toAddress: String,
        coin: Coin,
        remarks: String
    ) {
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
        ApiService.getDipApi().txs(reqBroadCast).subscribeBy({
            if (it.success()) {
                AccountManager.instance().refresh()
                resultLD.value = Pair(true, getString(R.string.succeed))
            } else {
                resultLD.value = Pair(false, getString(R.string.failed))
            }

        }, {
            resultLD.value = Pair(false, it)
        }).add()
    }
}