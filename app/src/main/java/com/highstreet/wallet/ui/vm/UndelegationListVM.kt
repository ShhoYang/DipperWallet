package com.highstreet.wallet.ui.vm

import com.highstreet.lib.viewmodel.BaseListViewModel
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.http.subscribeBy
import com.highstreet.wallet.model.req.Coin
import com.highstreet.wallet.model.res.DelegationInfo

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

class UndelegationListVM : BaseListViewModel<DelegationInfo>() {

    override fun loadData(page: Int, onResponse: (ArrayList<DelegationInfo>?) -> Unit) {
        ApiService.getDipApi().unBondingDelegations(AccountManager.instance().address, page, pageSize()).subscribeBy({
            onResponse(handle(it.result))
        }, {
            onResponse(null)
        }).add()
    }

    private fun handle(list: ArrayList<DelegationInfo>?): ArrayList<DelegationInfo>? {
        if (list == null || list.isEmpty()) {
            return list
        }

        val temp = ArrayList<DelegationInfo>()

        list.forEach { d ->
            d.entries?.forEach { e ->
                val delegationInfo = DelegationInfo(Coin(e.balance, ""), "", null, e.initial_balance, d.validator_address)
                delegationInfo.completionTime = e.completion_time
                temp.add(delegationInfo)
            }
        }

        return temp
    }
}