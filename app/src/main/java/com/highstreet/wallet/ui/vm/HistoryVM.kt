package com.highstreet.wallet.ui.vm

import com.hao.library.http.subscribeBy2
import com.hao.library.viewmodel.BaseListViewModel
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.constant.Chain
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.model.res.Tx

/**
 * @author Yang Shihao
 * @Date 2020/10/19
 */
class HistoryVM : BaseListViewModel<Tx>() {

    override fun loadData(page: Int, onResponse: (ArrayList<Tx>?) -> Unit) {
        ApiService.getApi(Chain.DIP_MAIN)
            .txHistory(AccountManager.instance().address, page, pageSize())
            .subscribeBy2({
                onResponse(it)
            }, {
                onResponse(null)
            }).add()
    }
}
