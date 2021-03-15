package com.highstreet.wallet.ui.vm

import androidx.lifecycle.MutableLiveData
import com.hao.library.http.subscribeBy
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.model.res.DelegationInfo
import com.highstreet.wallet.utils.StringUtils

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */

class WalletVM : BalanceVM() {

    val delegationAmountLD = MutableLiveData<String>()
    val undelegationAmountLD = MutableLiveData<String>()
    val rewardD: MutableLiveData<String> = MutableLiveData()
    val inflationD: MutableLiveData<String> = MutableLiveData()

    fun loadData(address: String) {
        getAccountInfo(address)
        getDelegationAmount(address)
        getUnbondingDelegationAmount(address)
        getRewards(address)
        getInflation()
    }

    private fun getDelegationAmount(address: String) {
        ApiService.getApi().delegations(address, 1, Int.MAX_VALUE)
            .subscribeBy({
                processDelegations(it)
            }, {
                processDelegations(null)
            }).add()
    }

    private fun processDelegations(list: ArrayList<DelegationInfo>?) {
        if (list == null || list.isEmpty()) {
            delegationAmountLD.value = "0"
            return
        }
        var amount = 0L

        list?.forEach {
            amount += it.shares?.toDouble()?.toLong() ?: 0
        }

        delegationAmountLD.value = StringUtils.pdip2DIP(amount.toString())
    }

    private fun getUnbondingDelegationAmount(address: String) {
        ApiService.getApi().unbondingDelegations(address, 1, Int.MAX_VALUE)
            .subscribeBy({
                processUnbondingDelegations(it)
            }, {
                processUnbondingDelegations(null)
            }).add()
    }

    private fun processUnbondingDelegations(list: ArrayList<DelegationInfo>?) {
        if (list == null || list.isEmpty()) {
            undelegationAmountLD.value = "0"
            return
        }
        var amount = 0L

        list?.forEach {
            it?.entries?.forEach { e ->
                amount += e?.balance?.toDouble()?.toLong()
            }
        }
        undelegationAmountLD.value = StringUtils.pdip2DIP(amount.toString())
    }

    private fun getRewards(address: String) {
        ApiService.getApi().rewards(address).subscribeBy({
            rewardD.value = it?.getTotalReward() ?: ""
        }, {
            rewardD.value = ""
        }).add()
    }

    private fun getInflation() {
        ApiService.getApi().getInflation().subscribeBy({
            inflationD.value = StringUtils.formatPercent(it)
        }, {
            inflationD.value = ""
        }).add()
    }
}