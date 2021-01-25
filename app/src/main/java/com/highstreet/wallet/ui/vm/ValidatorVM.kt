package com.highstreet.wallet.ui.vm

import android.os.Handler
import android.os.Looper
import com.highstreet.lib.viewmodel.BaseListViewModel
import com.highstreet.wallet.constant.Constant
import com.highstreet.wallet.constant.SortType
import com.highstreet.wallet.constant.ValidatorType
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.http.subscribeBy
import com.highstreet.wallet.model.res.Validator

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
class ValidatorVM : BaseListViewModel<Validator>() {

    var filterType =ValidatorType.ALL

    private var sortType = SortType.SHARES_DESC

    private val handler = Handler()

    private val list = ArrayList<Validator>()

    fun filter(filterType: Int, sortType: Int) {
        if (this.filterType != filterType || this.sortType != sortType) {
            this.filterType = filterType
            this.sortType = sortType
            invalidate()
        }
    }

    override fun pageSize() = Int.MAX_VALUE

    override fun loadData(page: Int, onResponse: (ArrayList<Validator>?) -> Unit) {

        if (list.isEmpty()) {
            ApiService.getDipApi().validators(page, pageSize()).subscribeBy({
                list.clear()
                val data = it.result
                if (data != null && data.isNotEmpty()) {
                    list.addAll(data)
                }
                filterData(onResponse)
            }, {
                onResponse(null)
            }).add()
        } else {
            filterData(onResponse)
        }
    }

    private fun filterData(onResponse: (ArrayList<Validator>?) -> Unit) {
        val ret = ArrayList<Validator>()
        when (filterType) {
            ValidatorType.BONDED -> {
                val temp = list.filter {
                    ValidatorType.BONDED == it.status
                }
                if (temp.isNotEmpty()) {
                    ret.addAll(temp)
                }
            }
            ValidatorType.JAILED -> {
                val temp = list.filter {
                    it.jailed == true
                }
                if (temp.isNotEmpty()) {
                    ret.addAll(temp)
                }
            }
            else -> {
                if (list.isNotEmpty()) {
                    ret.addAll(list)
                }
            }
        }

        ret.sortBy { v ->
            when (sortType) {
                SortType.RATE_DESC -> (v.commission?.commission_rates?.rate?.toDouble()
                        ?: 0.0) / (-1.0)
                SortType.SHARES_DESC -> (v.delegator_shares?.toDouble() ?: 0.0) / (-1.0)
                SortType.SHARES_ASC -> v.delegator_shares?.toDouble() ?: 0.0
                else -> v.commission?.commission_rates?.rate?.toDouble() ?: 0.0
            }
        }

        if (Looper.myLooper() == Looper.getMainLooper()) {
            onResponse(ret)
        } else {
            handler.post {
                onResponse(ret)
            }
        }
    }
}