package com.highstreet.wallet.ui.vm

import androidx.lifecycle.MutableLiveData
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.http.subscribeBy
import com.highstreet.wallet.model.res.DelegationInfo

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */

open class CapitalVM : BalanceVM() {

    val delegationAmountLD = MutableLiveData<String>()

    fun getDelegationAmount(address: String) {
        ApiService.getDipApi().delegations(address, 1, Int.MAX_VALUE)
            .subscribeBy({
                handle(it.result)
            }, {
                handle(null)
            }).add()
    }

    private fun handle(list: ArrayList<DelegationInfo>?) {
        if (list == null || list.isEmpty()) {
            delegationAmountLD.value = "0"
            return
        }
        var amount = 0L

        list?.forEach {
            amount += it.shares?.toDouble()?.toLong() ?: 0
        }

        delegationAmountLD.value = amount.toString()
    }
}