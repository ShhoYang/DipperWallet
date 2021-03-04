package com.highstreet.wallet.ui.fragment

import android.view.View
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.databinding.FragmentBaseListBinding
import com.hao.library.ui.BaseListFragment
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.model.res.DelegationInfo
import com.highstreet.wallet.ui.activity.DelegationDetailActivity
import com.highstreet.wallet.ui.adapter.UndelegationAdapter
import com.highstreet.wallet.ui.vm.UndelegationListVM
import kotlin.properties.Delegates

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

@AndroidEntryPoint
class UndelegationListFragment :
    BaseListFragment<FragmentBaseListBinding, DelegationInfo, UndelegationListVM, UndelegationAdapter>() {

    private var refresh: Int by Delegates.observable(0) { _, old, new ->
        if (old != new) {
            vm?.refresh()
        }
    }

    override fun onResume() {
        super.onResume()
        refresh = AccountManager.instance().refresh
    }

    override fun itemClicked(view: View, item: DelegationInfo, position: Int) {
        activity?.let { DelegationDetailActivity.start(it, item, true) }
    }
}