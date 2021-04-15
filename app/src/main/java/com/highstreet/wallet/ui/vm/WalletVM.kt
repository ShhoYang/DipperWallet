package com.highstreet.wallet.ui.vm

import androidx.lifecycle.MutableLiveData
import com.hao.library.http.subscribeBy
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.utils.StringUtils
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */

class WalletVM : NativeTokenDetailVM() {

    val inflationLD: MutableLiveData<String> = MutableLiveData()
    val averageYieldLD: MutableLiveData<String> = MutableLiveData()

    override fun loadData(address: String) {
        super.loadData(address)
        getInflation()
        getAverageYield()
    }

    private fun getInflation() {
        ApiService.getApi().getInflation().subscribeBy({
            inflationLD.value = StringUtils.formatPercent(it)
        }, {
            inflationLD.value = ""
        }).add()
    }


    private fun getAverageYield() {
        var stakingPool: String? = null
        ApiService.getApi().getStakingPool()
            .flatMap {
                stakingPool = it.result?.bonded_tokens
                return@flatMap ApiService.getApi().getProvisions()
            }
            .subscribeBy({
                averageYieldLD.value = formatAverageYield(stakingPool, it)
            }, {
                averageYieldLD.value = ""
            }).add()
    }

    private fun formatAverageYield(stakingPool: String?, provisions: String?): String {
        if (stakingPool == null
            || stakingPool == ""
            || provisions == null
            || provisions == ""
        ) {
            return ""
        }

        try {
            val ret =
                BigDecimal(provisions).divide(BigDecimal(stakingPool), 6, RoundingMode.HALF_UP)
                    .setScale(2, RoundingMode.DOWN).toPlainString()
            return StringUtils.formatPercent(ret)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}