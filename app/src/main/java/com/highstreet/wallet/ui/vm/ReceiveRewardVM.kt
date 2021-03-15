package com.highstreet.wallet.ui.vm

import androidx.lifecycle.MutableLiveData
import com.hao.library.http.subscribeBy
import com.hao.library.http.subscribeBy2
import com.hao.library.viewmodel.BaseViewModel
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.model.req.RequestBroadCast
import com.highstreet.wallet.model.res.AccountInfo
import com.highstreet.wallet.utils.AmountUtils
import com.highstreet.wallet.crypto.KeyUtils
import com.highstreet.wallet.utils.MsgGeneratorUtils

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */

class ReceiveRewardVM : BaseViewModel() {

    val resultLD = MutableLiveData<Pair<Boolean, String>>()

    fun receiveReward(validatorAddress: String, delegatorAddress: String) {
        ApiService.getApi().account(AccountManager.instance().address).subscribeBy({
            val coins = it?.value?.coins
            if (null != coins && coins.isNotEmpty()) {
                generateParams(it, validatorAddress, delegatorAddress)
            }
        }, {
            resultLD.value = Pair(false, "")
        }).add()
    }

    private fun generateParams(
        accountInfo: AccountInfo,
        validatorAddress: String,
        delegatorAddress: String
    ) {
        val account = AccountManager.instance().account!!
        account.accountNumber = accountInfo.getAccountNumber()
        account.sequenceNumber = accountInfo.getSequence()
        val deterministicKey =
            KeyUtils.getDeterministicKey(account.chain, account.getEntropyAsHex(), account.path)
        val msg = MsgGeneratorUtils.receiveRewardMsg(
            validatorAddress,
            delegatorAddress,
            account.chain
        )
        doReceiveReward(
            MsgGeneratorUtils.getBroadCast(
                account,
                msg,
                AmountUtils.generateFee(),
                "",
                deterministicKey
            )
        )
    }

    private fun doReceiveReward(reqBroadCast: RequestBroadCast) {
        ApiService.getApi().txs(reqBroadCast).subscribeBy2({
            if (true == it?.success()) {
                resultLD.value = Pair(true, "")
            } else {
                resultLD.value = Pair(false, "")
            }
        }, {
            resultLD.value = Pair(false, it.second)
        }).add()
    }
}