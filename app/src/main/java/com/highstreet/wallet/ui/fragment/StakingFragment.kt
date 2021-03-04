package com.highstreet.wallet.ui.fragment

import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseViewPagerFragment
import com.hao.library.ui.FragmentCreator
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.R
import com.highstreet.wallet.databinding.FragmentStakingBinding
import com.highstreet.wallet.ui.activity.DelegationTransactionRecordActivity
import com.highstreet.wallet.view.listener.RxView

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
@AndroidEntryPoint(injectViewModel = false)
class StakingFragment : BaseViewPagerFragment<FragmentStakingBinding, PlaceholderViewModel>() {

    override fun initView() {
        super.initView()
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