package com.highstreet.wallet.ui.fragment

import android.view.View
import com.highstreet.lib.ui.BaseListFragment
import com.highstreet.wallet.model.res.DelegationInfo
import com.highstreet.wallet.ui.activity.DelegationDetailActivity
import com.highstreet.wallet.ui.adapter.UndelegationAdapter
import com.highstreet.wallet.ui.vm.UndelegationListVM


/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

class UndelegationListFragment : BaseListFragment<DelegationInfo, UndelegationListVM>() {

    override fun createAdapter() = UndelegationAdapter()

    override fun initData() {
        lifecycle.addObserver(viewModel)
        super.initData()
    }

    override fun itemClicked(view: View, item: DelegationInfo, position: Int) {
        activity?.let { DelegationDetailActivity.start(it, item, true) }
    }
}