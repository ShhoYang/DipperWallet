package com.highstreet.wallet.ui.vm

import androidx.lifecycle.MutableLiveData
import com.hao.library.http.subscribeBy
import com.hao.library.http.subscribeBy2
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.utils.AmountUtils

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */

open class NativeTokenDetailVM : BalanceVM() {

    val delegationAmountLD = MutableLiveData<String>()
    val unbondingDelegationAmountLD = MutableLiveData<String>()
    val rewardLD: MutableLiveData<String> = MutableLiveData()

    open fun loadData(address: String) {
        getAccountInfo(address)
        getDelegationAmount(address)
        getUnbondingDelegationAmount(address)
        getRewards(address)
    }

    private fun getDelegationAmount(address: String) {
        ApiService.getApi().delegations(address, 1, Int.MAX_VALUE)
            .map { return@map AmountUtils.processDelegations(it.result) }
            .subscribeBy2({
                delegationAmountLD.value = it
            }, {
                delegationAmountLD.value = AmountUtils.ZERO
            }).add()
    }

    private fun getUnbondingDelegationAmount(address: String) {
        ApiService.getApi().unbondingDelegations(address, 1, Int.MAX_VALUE)
            .map { return@map AmountUtils.processUnbondingDelegations(it.result) }
            .subscribeBy2({
                unbondingDelegationAmountLD.value = it
            }, {
                unbondingDelegationAmountLD.value = AmountUtils.ZERO
            }).add()
    }

    private fun getRewards(address: String) {
        ApiService.getApi().rewards(address).subscribeBy({
            rewardLD.value = it?.getTotalReward() ?: AmountUtils.ZERO
        }, {
            rewardLD.value = AmountUtils.ZERO
        }).add()
    }
}