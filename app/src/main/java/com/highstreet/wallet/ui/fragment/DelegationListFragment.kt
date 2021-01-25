package com.highstreet.wallet.ui.fragment

import androidx.lifecycle.Observer
import android.view.View
import com.highstreet.lib.ui.BaseListFragment
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.db.Db
import com.highstreet.wallet.model.res.DelegationInfo
import com.highstreet.wallet.utils.StringUtils
import com.highstreet.wallet.ui.activity.DelegationDetailActivity
import com.highstreet.wallet.ui.activity.ValidatorListActivity
import com.highstreet.wallet.ui.adapter.DelegationAdapter
import com.highstreet.wallet.ui.vm.DelegationListVM
import kotlinx.android.synthetic.main.g_fragment_delegation_list.*

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

class DelegationListFragment : BaseListFragment<DelegationInfo, DelegationListVM>() {

    override fun getLayoutId() = R.layout.g_fragment_delegation_list

    override fun createAdapter() = DelegationAdapter()

    override fun initView() {
        super.initView()
        RxView.click(flValidator) {
            to(ValidatorListActivity::class.java)
        }
    }

    override fun initData() {
        lifecycle.addObserver(viewModel)
        Db.instance().accountDao().queryLastUserAsLiveData(true).observe(this, Observer {
            tvReceiveAddress.text = AccountManager.instance().address
            viewModel.invalidate()
        })
        viewModel.totalLD.observe(this, Observer {
            tvDelegationAmount.text = StringUtils.pdip2DIP(it?.first)
            tvValidatorCount.text = it?.second
        })
        viewModel.rewardD.observe(this, Observer {
            tvReward.text = StringUtils.pdip2DIP(it)
        })
        super.initData()
    }

    override fun itemClicked(view: View, item: DelegationInfo, position: Int) {
        activity?.let { DelegationDetailActivity.start(it, item) }
    }
}