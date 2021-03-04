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
 * @Date 2020/10/28
 */

class RedelegationVM : BaseViewModel() {

    val redelegateLD: MutableLiveData<Pair<Boolean, String>> = MutableLiveData()

    fun redelegate(amount: String, delegationInfo: DelegationInfo, toValidatorAddress: String) {
        ApiService.getDipApi().account(AccountManager.instance().address).subscribeBy({
            if (it == null) {
                redelegateLD.value = Pair(false, "")
            } else {
                generateParams(it, amount, delegationInfo, toValidatorAddress)
            }
        }, {
            redelegateLD.value = Pair(false, "")
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
        ApiService.getDipApi().txs(reqBroadCast).subscribeBy2({
            if (true == it?.success()) {
                AccountManager.instance().refresh()
                redelegateLD.value = Pair(true, "")
            } else {
                redelegateLD.value = Pair(false, "")
            }

        }, {
            redelegateLD.value = Pair(false, it.second)
        }).add()
    }
}