package com.highstreet.wallet.ui.activity

import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseViewPagerActivity
import com.hao.library.ui.FragmentCreator
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.R
import com.highstreet.wallet.databinding.ActivityStakingBinding
import com.highstreet.wallet.ui.fragment.DelegationListFragment
import com.highstreet.wallet.ui.fragment.UndelegationListFragment
import com.highstreet.wallet.view.listener.RxView

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
@AndroidEntryPoint(injectViewModel = false)
class StakingActivity : BaseViewPagerActivity<ActivityStakingBinding, PlaceholderViewModel>() {

    override fun initView() {
        super.initView()
        setTitle(R.string.myDelegation)
        viewBinding {
            RxView.click(ivRecord) {
                toA(DelegationTransactionRecordActivity::class.java)
            }
        }
    }

    override fun getTitles(): List<String> {
        return arrayListOf(
            getString(R.string.bond),
            getString(R.string.unbond)
        )
    }

    override fun getFragments(): List<FragmentCreator> {
        return arrayListOf(
            object : FragmentCreator {
                override fun createFragment() = DelegationListFragment()
            },
            object : FragmentCreator {
                override fun createFragment() = UndelegationListFragment()
            }
        )
    }
}