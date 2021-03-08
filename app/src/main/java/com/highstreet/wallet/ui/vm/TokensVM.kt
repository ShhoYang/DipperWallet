package com.highstreet.wallet.ui.vm

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.hao.library.http.subscribeBy
import com.hao.library.viewmodel.BaseListViewModel
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.model.res.AccountInfo
import com.highstreet.wallet.model.res.Token

/**
 * @author Yang Shihao
 * @Date 3/6/21
 */
class TokensVM : BaseListViewModel<Token>() {

    private val handle = Handler(Looper.getMainLooper())

    val amountLD = MutableLiveData<AccountInfo>()

    fun getAccountInfo(address: String) {
        ApiService.getDipApi().account(address).subscribeBy({
            amountLD.value = it
        }, {
            amountLD.value = null
        }).add()
    }

    override fun loadData(page: Int, onResponse: (ArrayList<Token>?) -> Unit) {
        handle.post {
            onResponse(arrayListOf())
        }
    }
}