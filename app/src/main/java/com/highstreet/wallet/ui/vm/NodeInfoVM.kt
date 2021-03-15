package com.highstreet.wallet.ui.vm

import androidx.lifecycle.MutableLiveData
import com.hao.library.http.subscribeBy2
import com.hao.library.viewmodel.BaseViewModel
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.model.res.NodeInfoBean

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */
 class NodeInfoVM : BaseViewModel() {

    val nodeInfoLD = MutableLiveData<NodeInfoBean>()

    fun getNodeInfo() {
        ApiService.getApi().nodeInfo().subscribeBy2({
            nodeInfoLD.value = it
        }, {
            nodeInfoLD.value = null
        }).add()
    }
}