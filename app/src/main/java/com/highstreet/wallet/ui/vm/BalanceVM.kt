package com.highstreet.wallet.ui.vm

import androidx.lifecycle.MutableLiveData
import com.highstreet.lib.viewmodel.BaseViewModel
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.http.subscribeBy
import com.highstreet.wallet.model.res.AccountInfo

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */

open class BalanceVM : BaseViewModel() {

    val amountLD = MutableLiveData<AccountInfo>()

    fun getAccountInfo(address: String) {
        ApiService.getDipApi().account(address).subscribeBy({
            amountLD.value = it.result
        }, {
            amountLD.value = null
        }).add()
    }
}