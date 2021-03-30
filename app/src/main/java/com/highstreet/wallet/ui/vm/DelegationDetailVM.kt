package com.highstreet.wallet.ui.vm

import androidx.lifecycle.MutableLiveData
import com.hao.library.http.subscribeBy
import com.hao.library.http.subscribeBy2
import com.hao.library.viewmodel.BaseViewModel
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.model.res.Validator
import com.highstreet.wallet.utils.AmountUtils

/**
 * @author Yang Shihao
 * @Date 2020/10/27
 */

class DelegationDetailVM : BaseViewModel() {

    val validatorLD: MutableLiveData<Validator?> = MutableLiveData()
    val rewardLD: MutableLiveData<String> = MutableLiveData()

    fun getValidator(validatorAddress: String) {
        ApiService.getApi().validatorDetail(validatorAddress).subscribeBy({
            validatorLD.value = it
        }, {
            validatorLD.value = null
        }).add()
    }

    fun getReward(validatorAddress: String) {
        ApiService.getApi().rewardsByValidator(AccountManager.instance().address, validatorAddress)
            .map {
                val result = it.result
                return@map if (result == null || result.isEmpty()) {
                    AmountUtils.ZERO
                } else {
                    AmountUtils.pdip2DIP(result[0], false)
                }
            }
            .subscribeBy2({
                rewardLD.value = it
            }, {
                rewardLD.value = "0"
            }).add()
    }
}