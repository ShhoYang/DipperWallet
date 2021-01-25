package com.highstreet.wallet.ui.vm

import androidx.lifecycle.MutableLiveData
import com.highstreet.lib.viewmodel.BaseViewModel
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.http.subscribeBy
import com.highstreet.wallet.model.res.Validator
import com.highstreet.wallet.utils.StringUtils

/**
 * @author Yang Shihao
 * @Date 2020/10/27
 */

class DelegationDetailVM : BaseViewModel() {

    val validatorLD: MutableLiveData<Validator?> = MutableLiveData()
    val rewardLD: MutableLiveData<String> = MutableLiveData()

    fun getValidator(validatorAddress: String) {
        ApiService.getDipApi().validatorDetail(validatorAddress).subscribeBy({
            validatorLD.value = it?.result
        }, {
            validatorLD.value = null
        }).add()
    }

    fun getReward(validatorAddress: String) {
        ApiService.getDipApi().rewardsByValidator(AccountManager.instance().address, validatorAddress).subscribeBy({
            val list = it.result
            rewardLD.value = if (list == null || list.isEmpty()) {
                "0"
            } else {
                val coin = list[0]
                StringUtils.pdip2DIP(coin, false)
            }
        }, {
            rewardLD.value = "0"
        }).add()
    }
}