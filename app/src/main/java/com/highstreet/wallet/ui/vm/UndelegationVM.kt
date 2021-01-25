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
 * @Date 2020/10/27
 */

class UndelegationVM : BaseViewModel() {

    val undelegateLD: MutableLiveData<Pair<Boolean, String>> = MutableLiveData()

    fun undelegate(amount: String, delegationInfo: DelegationInfo) {
        ApiService.getDipApi().account(AccountManager.instance().address).subscribeBy({
            if (it.result == null) {
                undelegateLD.value = Pair(false, "解委托失败")
            } else {
                generateParams(it.result!!, amount, delegationInfo)
            }
        }, {
            undelegateLD.value = Pair(false, "解委托失败")
        }).add()

    }

    private fun generateParams(
        accountInfo: AccountInfo,
        amount: String,
        delegationInfo: DelegationInfo
    ) {
        val account = AccountManager.instance().account!!
        account.accountNumber = accountInfo.getAccountNumber()
        account.sequenceNumber = accountInfo.getSequence()
        val deterministicKey =
            KeyUtils.getDeterministicKey(account.chain, account.getEntropyAsHex(), account.path)
        val msg = MsgGeneratorUtils.undelegateMsg(
            delegationInfo.delegator_address,
            delegationInfo.validator_address,
            AmountUtils.generateCoin(amount),
            account.chain
        )
        doUndelegate(
            MsgGeneratorUtils.getBroadCast(
                account,
                msg,
                AmountUtils.generateFee(),
                "",
                deterministicKey
            )
        )
    }

    private fun doUndelegate(reqBroadCast: RequestBroadCast) {
        ApiService.getDipApi().txs(reqBroadCast).subscribeBy({
            if (it.success()) {
                RxBus.instance().send(RefreshDelegationEvent())
                undelegateLD.value = Pair(true, "解委托成功")
            } else {
                undelegateLD.value = Pair(false, "解委托失败")
            }

        }, {
            undelegateLD.value = Pair(false, it)
        }).add()
    }
}