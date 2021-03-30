package com.highstreet.wallet.ui.vm

import androidx.lifecycle.MutableLiveData
import com.hao.library.http.subscribeBy
import com.hao.library.viewmodel.BaseViewModel
import com.highstreet.wallet.crypto.ContractUtils
import com.highstreet.wallet.http.ApiService

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */

open class TokenBalanceVM : BaseViewModel() {

    val amountLD = MutableLiveData<String>()

    fun getAccountInfo(address: String) {
        ApiService.getApi().estimateGas(ContractUtils.balance(address)).subscribeBy({
            amountLD.value = ContractUtils.decodeNumber(it?.Res) ?: "0"
        }, {
            amountLD.value = null
        }).add()
    }
}