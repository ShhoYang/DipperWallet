package com.highstreet.wallet.ui.vm

import androidx.lifecycle.MutableLiveData
import com.hao.library.http.subscribeBy
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.utils.StringUtils

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */

class WalletVM : NativeTokenDetailVM() {

    val inflationD: MutableLiveData<String> = MutableLiveData()

    override fun loadData(address: String) {
        super.loadData(address)
        getInflation()
    }

    private fun getInflation() {
        ApiService.getApi().getInflation().subscribeBy({
            inflationD.value = StringUtils.formatPercent(it)
        }, {
            inflationD.value = ""
        }).add()
    }
}