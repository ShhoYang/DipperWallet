package com.highstreet.wallet.ui.vm

import com.hao.library.http.subscribeBy2
import com.hao.library.viewmodel.BaseListViewModel
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.model.res.Tx

/**
 * @author Yang Shihao
 * @Date 2020/10/19
 */
class TransactionRecordVM : BaseListViewModel<Tx>() {

    var isIn = true

    override fun loadData(page: Int, onResponse: (ArrayList<Tx>?) -> Unit) {
        if (isIn) {
            ApiService.getApi()
                .transactionInRecord(AccountManager.instance().address, page, pageSize())
                .subscribeBy2({
                    onResponse(it?.txs)
                }, {
                    onResponse(null)
                }).add()
        } else {
            ApiService.getApi()
                .transactionOutRecord(AccountManager.instance().address, page, pageSize())
                .subscribeBy2({
                    onResponse(it?.txs)
                }, {
                    onResponse(null)
                }).add()
        }
    }
}
