package com.highstreet.wallet.ui.vm

import android.os.Handler
import android.os.Looper
import com.hao.library.http.subscribeBy
import com.hao.library.viewmodel.BaseListViewModel
import com.highstreet.wallet.constant.SortType
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.model.res.Validator

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
class AllValidatorVM : BaseListViewModel<Validator>() {

    private var sortType = SortType.SHARES_DESC

    private val handler = Handler()

    private val list = ArrayList<Validator>()

    fun filter(sortType: Int) {
        if (this.sortType != sortType) {
            this.sortType = sortType
            refresh()
        }
    }

    override fun pageSize() = Int.MAX_VALUE

    override fun loadData(page: Int, onResponse: (ArrayList<Validator>?) -> Unit) {
        if (list.isEmpty()) {
            ApiService.getApi().validators(page, pageSize()).subscribeBy({
                list.clear()
                if (it != null && it.isNotEmpty()) {
                    list.addAll(it)
                }
                sortData(onResponse)
            }, {
                onResponse(null)
            }).add()
        } else {
            sortData(onResponse)
        }
    }

    private fun sortData(onResponse: (ArrayList<Validator>?) -> Unit) {
        list.sortBy { v ->
            when (sortType) {
                SortType.RATE_DESC -> (v.commission?.commission_rates?.rate?.toDouble()
                    ?: 0.0) / (-1.0)
                SortType.SHARES_DESC -> (v.delegator_shares?.toDouble() ?: 0.0) / (-1.0)
                SortType.SHARES_ASC -> v.delegator_shares?.toDouble() ?: 0.0
                else -> v.commission?.commission_rates?.rate?.toDouble() ?: 0.0
            }
        }

        if (Looper.myLooper() == Looper.getMainLooper()) {
            onResponse(list)
        } else {
            handler.post {
                onResponse(list)
            }
        }
    }
}