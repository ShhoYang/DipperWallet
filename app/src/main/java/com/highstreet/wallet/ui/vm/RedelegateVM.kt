package com.highstreet.wallet.ui.vm

import androidx.lifecycle.MutableLiveData
import com.hao.library.http.CustomException
import com.hao.library.http.subscribeBy2
import com.hao.library.viewmodel.BaseViewModel
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.App
import com.highstreet.wallet.R
import com.highstreet.wallet.crypto.KeyUtils
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.model.req.Coin
import com.highstreet.wallet.model.req.RequestBroadCast
import com.highstreet.wallet.model.res.AccountInfo
import com.highstreet.wallet.model.res.DelegationInfo
import com.highstreet.wallet.utils.AmountUtils
import com.highstreet.wallet.utils.MsgGeneratorUtils

/**
 * @author Yang Shihao
 * @Date 2020/10/28
 */

class RedelegateVM : BaseViewModel() {

    val redelegateLD: MutableLiveData<Pair<Boolean, String>> = MutableLiveData()

    fun redelegate(
        amount: String,
        delegationInfo: DelegationInfo,
        toValidatorAddress: String,
        memo: String
    ) {
        ApiService.getApi().account(AccountManager.instance().address)
            .flatMap {
                return@flatMap ApiService.getApi()
                    .txs(
                        generateParams(
                            it.result,
                            amount,
                            delegationInfo,
                            toValidatorAddress,
                            memo
                        )
                    )
            }.subscribeBy2({
                if (true == it?.success()) {
                    AccountManager.instance().refreshBalance()
                    redelegateLD.value = Pair(true, App.instance.getString(R.string.succeed))
                } else {
                    redelegateLD.value = Pair(false, App.instance.getString(R.string.failed))
                }
            }, {
                redelegateLD.value = Pair(false, it.errorMsg)
            }).add()
    }

    @Throws(CustomException::class)
    private fun generateParams(
        accountInfo: AccountInfo?,
        amount: String,
        delegationInfo: DelegationInfo,
        toValidatorAddress: String,
        memo: String
    ): RequestBroadCast {
        if (accountInfo == null) {
            throw   CustomException(R.string.failed)
        }
        val account = AccountManager.instance().account!!
        account.accountNumber = accountInfo.getAccountNumber()
        account.sequenceNumber = accountInfo.getSequence()
        val deterministicKey =
            KeyUtils.getDeterministicKey(account.chain, account.getEntropyAsHex(), account.path)
        val msg = MsgGeneratorUtils.redelegateMsg(
            account.address,
            delegationInfo.validator_address,
            toValidatorAddress,
            Coin.generateCoin(amount),
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