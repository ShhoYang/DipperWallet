package com.highstreet.wallet.ui.vm

import androidx.lifecycle.MutableLiveData
import com.highstreet.lib.viewmodel.BaseViewModel
import com.highstreet.lib.viewmodel.RxBus
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.event.RefreshDelegationEvent
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.http.subscribeBy
import com.highstreet.wallet.model.req.RequestBroadCast
import com.highstreet.wallet.model.res.AccountInfo
import com.highstreet.wallet.model.res.DelegationInfo
import com.highstreet.wallet.utils.AmountUtils
import com.highstreet.wallet.crypto.KeyUtils
import com.highstreet.wallet.utils.MsgGeneratorUtils

/**
 * @author Yang Shihao
 * @Date 2020/10/28
 */

class RedelegationVM : BaseViewModel() {

    val redelegateLD: MutableLiveData<Pair<Boolean, String>> = MutableLiveData()

    fun redelegate(amount: String, delegationInfo: DelegationInfo, toValidatorAddress: String) {
        ApiService.getDipApi().account(AccountManager.instance().address).subscribeBy({
            if (it.result == null) {
                redelegateLD.value = Pair(false, "转委托失败")
            } else {
                generateParams(it.result!!, amount, delegationInfo, toValidatorAddress)
            }
        }, {
            redelegateLD.value = Pair(false, "转委托失败")
        }).add()

    }

    private fun generateParams(
        accountInfo: AccountInfo,
        amount: String,
        delegationInfo: DelegationInfo,
        toValidatorAddress: String
    ) {
        val account = AccountManager.instance().account!!
        account.accountNumber = accountInfo.getAccountNumber()
        account.sequenceNumber = accountInfo.getSequence()
        val deterministicKey =
            KeyUtils.getDeterministicKey(account.chain, account.getEntropyAsHex(), account.path)
        val msg = MsgGeneratorUtils.redelegateMsg(
            account.address,
            delegationInfo.validator_address,
            toValidatorAddress,
            AmountUtils.generateCoin(amount),
            account.chain
        )
        doRedelegate(
            MsgGeneratorUtils.getBroadCast(
                account,
                msg,
                AmountUtils.generateFee(),
                "",
                deterministicKey
            )
        )
    }

    private fun doRedelegate(reqBroadCast: RequestBroadCast) {
        ApiService.getDipApi().txs(reqBroadCast).subscribeBy({
            if (it.success()) {
                RxBus.instance().send(RefreshDelegationEvent())
                redelegateLD.value = Pair(true, "转委托成功")
            } else {
                redelegateLD.value = Pair(false, "转委托失败")
            }

        }, {
            redelegateLD.value = Pair(false, it)
        }).add()
    }
}