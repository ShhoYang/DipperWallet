package com.highstreet.wallet.ui.vm

import androidx.lifecycle.MutableLiveData
import com.hao.library.http.subscribeBy
import com.hao.library.viewmodel.BaseViewModel
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.model.res.AccountInfo

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */

open class BalanceVM : BaseViewModel() {

    val amountLD = MutableLiveData<AccountInfo>()

    fun getAccountInfo(address: String) {
        ApiService.getApi().account(address).subscribeBy({
            amountLD.value = it
        }, {
            amountLD.value = null
        }).add()
    }
}