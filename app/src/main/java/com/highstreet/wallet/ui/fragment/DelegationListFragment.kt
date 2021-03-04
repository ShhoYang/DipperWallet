package com.highstreet.wallet.ui.fragment

import androidx.lifecycle.Observer
import android.view.View
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseListFragment
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.databinding.FragmentDelegationListBinding
import com.highstreet.wallet.db.Db
import com.highstreet.wallet.model.res.DelegationInfo
import com.highstreet.wallet.utils.StringUtils
import com.highstreet.wallet.ui.activity.DelegationDetailActivity
import com.highstreet.wallet.ui.activity.ValidatorListActivity
import com.highstreet.wallet.ui.adapter.DelegationAdapter
import com.highstreet.wallet.ui.vm.DelegationListVM
import com.highstreet.wallet.view.listener.RxView
import kotlin.properties.Delegates

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

@AndroidEntryPoint
class DelegationListFragment :
    BaseListFragment<FragmentDelegationListBinding, DelegationInfo, DelegationListVM, DelegationAdapter>() {

    private var refresh: Int by Delegates.observable(0) { _, old, new ->
        if (old != new) {
            vm?.refresh()
        }
    }

    override fun onResume() {
        super.onResume()
        refresh = AccountManager.instance().refresh
    }

    override fun initView() {
        super.initView()
        viewBinding {
            RxView.click(flValidator) {
                toA(ValidatorListActivity::class.java)
            }
        }
    }

    override fun initData() {
        Db.instance().accountDao().queryLastUserAsLiveData(true).observe(this, Observer {
            vb?.tvReceiveAddress?.text = AccountManager.instance().address
            vm?.refresh()
        })
        viewModel {
            totalLD.observe(this@DelegationListFragment, Observer {
                viewBinding {
                    tvDelegationAmount.text = StringUtils.pdip2DIP(it?.first)
                    tvValidatorCount.text = it?.second
                }
            })
            rewardD.observe(this@DelegationListFragment, Observer {
                vb?.tvReward?.text = StringUtils.pdip2DIP(it)
            })
        }
        super.initData()
    }

    override fun itemClicked(view: View, item: DelegationInfo, position: Int) {
        activity?.let { DelegationDetailActivity.start(it, item) }
    }
}