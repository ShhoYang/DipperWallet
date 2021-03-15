package com.highstreet.wallet.ui.vm

import com.hao.library.http.subscribeBy
import com.hao.library.viewmodel.BaseListViewModel
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.ui.fragment.DelegationTransactionRecordFragment
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.model.res.Tx

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

class DelegationTransactionRecordVM : BaseListViewModel<Tx>() {

    var type = DelegationTransactionRecordFragment.TYPE_BOND

    override fun loadData(page: Int, onResponse: (ArrayList<Tx>?) -> Unit) {
        ApiService.getApi()
            .delegationTransactionRecord(AccountManager.instance().address, type, page, pageSize())
            .subscribeBy({
                onResponse(it?.get(0)?.txs)
            }, {
                onResponse(null)
            }).add()
    }
}