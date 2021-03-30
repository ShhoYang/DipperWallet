package com.highstreet.wallet.ui.vm

import androidx.lifecycle.MutableLiveData
import com.hao.library.http.subscribeBy
import com.hao.library.viewmodel.BaseViewModel
import com.highstreet.wallet.crypto.ContractUtils
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.utils.AmountUtils

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */

class TokenDetailVM : BaseViewModel() {

    private var balance: String = "0"

    val amountLD = MutableLiveData<String>()

    fun loadData(address: String) {
        getAccountInfo(address)
    }

    fun getAccountInfo(address: String) {
        ApiService.getApi().estimateGas(ContractUtils.balance(address))
            .flatMap {
                balance = ContractUtils.decodeNumber(it.result?.Res) ?: "0"
                return@flatMap ApiService.getApi().estimateGas(ContractUtils.decimals(address))
            }
            .subscribeBy({
                amountLD.value = AmountUtils.getTokenAmount(balance, ContractUtils.decodeNumber(it?.Res))
            }, {
                amountLD.value = AmountUtils.ZERO
            }).add()
    }
}