package com.highstreet.wallet.ui.vm

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.hao.library.http.subscribeBy
import com.hao.library.viewmodel.BaseListViewModel
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.model.res.DelegationInfo
import com.highstreet.wallet.utils.StringUtils

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

class DelegationListVM : BaseListViewModel<DelegationInfo>() {

    val totalLD: MutableLiveData<Pair<String, String>> = MutableLiveData()
    val rewardD: MutableLiveData<String> = MutableLiveData()

    override fun loadData(page: Int, onResponse: (ArrayList<DelegationInfo>?) -> Unit) {
        ApiService.getApi().delegations(AccountManager.instance().address, page, pageSize())
            .subscribeBy({
                onResponse(it)
                processData(it)
            }, {
                onResponse(null)
                processData(null)
            }).add()
    }

    private fun processData(list: ArrayList<DelegationInfo>?) {
        if (list == null || list.isEmpty()) {
            totalLD.value = Pair("0", "0")
            return
        }
        var amount = 0L
        var validators = HashSet<String>()

        list?.forEach {
            amount += it.shares?.toDouble()?.toLong() ?: 0
            validators.add(it.validator_address)
        }

        totalLD.value = Pair(StringUtils.pdip2DIP(amount.toString()), validators.size.toString())
    }

    override fun refresh(callback: PageKeyedDataSource.LoadInitialCallback<Int, DelegationInfo>) {
        ApiService.getApi().rewards(AccountManager.instance().address).subscribeBy({
            rewardD.value = it?.getTotalReward() ?: ""
        }, {
            rewardD.value = ""
        }).add()
        super.refresh(callback)
    }
}