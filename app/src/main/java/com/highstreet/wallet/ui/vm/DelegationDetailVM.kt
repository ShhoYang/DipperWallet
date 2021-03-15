package com.highstreet.wallet.ui.vm

import androidx.lifecycle.MutableLiveData
import com.hao.library.http.subscribeBy
import com.hao.library.viewmodel.BaseViewModel
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.http.ApiService
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
        ApiService.getApi().validatorDetail(validatorAddress).subscribeBy({
            validatorLD.value = it
        }, {
            validatorLD.value = null
        }).add()
    }

    fun getReward(validatorAddress: String) {
        ApiService.getApi().rewardsByValidator(AccountManager.instance().address, validatorAddress).subscribeBy({
            rewardLD.value = if (it == null || it.isEmpty()) {
                "0"
            } else {
                StringUtils.pdip2DIP(it[0], false)
            }
        }, {
            rewardLD.value = ""
        }).add()
    }
}