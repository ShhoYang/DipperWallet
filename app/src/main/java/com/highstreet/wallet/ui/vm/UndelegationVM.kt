package com.highstreet.wallet.ui.vm

import androidx.lifecycle.MutableLiveData
import com.hao.library.http.subscribeBy
import com.hao.library.http.subscribeBy2
import com.hao.library.viewmodel.BaseViewModel
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.crypto.KeyUtils
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.model.req.RequestBroadCast
import com.highstreet.wallet.model.res.AccountInfo
import com.highstreet.wallet.model.res.DelegationInfo
import com.highstreet.wallet.utils.AmountUtils
import com.highstreet.wallet.utils.MsgGeneratorUtils

/**
 * @author Yang Shihao
 * @Date 2020/10/27
 */

class UndelegationVM : BaseViewModel() {

    val undelegateLD: MutableLiveData<Pair<Boolean, String>> = MutableLiveData()

    fun undelegate(amount: String, delegationInfo: DelegationInfo) {
        ApiService.getApi().account(AccountManager.instance().address).subscribeBy({
            if (it == null) {
                undelegateLD.value = Pair(false, "")
            } else {
                generateParams(it, amount, delegationInfo)
            }
        }, {
            undelegateLD.value = Pair(false, "")
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
        ApiService.getApi().txs(reqBroadCast).subscribeBy2({
            if (true == it?.success()) {
                AccountManager.instance().refresh()
                undelegateLD.value = Pair(true, "")
            } else {
                undelegateLD.value = Pair(false, "")
            }

        }, {
            undelegateLD.value = Pair(false, it.second)
        }).add()
    }
}