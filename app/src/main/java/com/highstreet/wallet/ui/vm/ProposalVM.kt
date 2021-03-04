package com.highstreet.wallet.ui.vm

import com.hao.library.http.subscribeBy
import com.hao.library.viewmodel.BaseListViewModel
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.model.res.Proposal

/**
 * @author Yang Shihao
 * @Date 2020/10/28
 */
class ProposalVM : BaseListViewModel<Proposal>() {

    override fun pageSize() = Int.MAX_VALUE

    override fun loadData(page: Int, onResponse: (ArrayList<Proposal>?) -> Unit) {
        ApiService.getDipApi().proposals().subscribeBy({
            onResponse(it)
        }, {
            onResponse(null)
        }).add()

    }
}
