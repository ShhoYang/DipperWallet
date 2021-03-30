package com.highstreet.wallet.ui.vm

import androidx.lifecycle.MutableLiveData
import com.hao.library.http.CustomException
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
        isAll: Boolean,
        memo: String
    ) {
        ApiService.getApi().account(AccountManager.instance().address)
            .flatMap {
                return@flatMap ApiService.getApi()
                    .txs(generateParams(it.result, toAddress, toAmount, isAll, memo))
            }.subscribeBy2({
                if (true == it?.success()) {
                    AccountManager.instance().refreshBalance()
                    resultLD.value = Pair(true, App.instance.getString(R.string.succeed))
                } else {
                    resultLD.value = Pair(false, App.instance.getString(R.string.failed))
                }
            }, {
                resultLD.value = Pair(false, it.errorMsg)
            }).add()
    }

    @Throws(CustomException::class)
    private fun generateParams(
        accountInfo: AccountInfo?,
        toAddress: String,
        toAmount: String,
        isAll: Boolean,
        memo: String
    ): RequestBroadCast {
        if (accountInfo == null) {
            throw   CustomException(R.string.failed)
        }
        var amount = AmountUtils.checkAmount(accountInfo, toAmount, isAll)
        val account = AccountManager.instance().account!!
        account.accountNumber = accountInfo.getAccountNumber()
        account.sequenceNumber = accountInfo.getSequence()
        val coinList = ArrayList<Coin>()
        coinList.add(amount)
        val deterministicKey =
            KeyUtils.getDeterministicKey(account.chain, account.getEntropyAsHex(), account.path)
        val msg = MsgGeneratorUtils.sendMsg(
            account.address,
            toAddress,
            coinList,
            account.chain
        )
        return MsgGeneratorUtils.getBroadCast(
            account,
            msg,
            AmountUtils.generateFee(),
            memo,
            deterministicKey
        )
    }
}