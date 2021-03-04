package com.highstreet.wallet.ui.activity

import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseViewPagerActivity
import com.hao.library.ui.FragmentCreator
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.R
import com.highstreet.wallet.databinding.ActivityViewpagerBinding
import com.highstreet.wallet.ui.fragment.DelegationTransactionRecordFragment

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
@AndroidEntryPoint(injectViewModel = false)
class DelegationTransactionRecordActivity :
    BaseViewPagerActivity<ActivityViewpagerBinding, PlaceholderViewModel>() {

    override fun initView() {
        setTitle(R.string.delegationTransactionRecord)
        super.initView()
    }

    override fun getTitles(): List<String> {
        return arrayListOf(
            getString(R.string.bond),
            getString(R.string.unbond),
            getString(R.string.redelegate)
        )
    }

    override fun getFragments(): List<FragmentCreator> {
        return arrayListOf(
            object : FragmentCreator {
                override fun createFragment() =
                    DelegationTransactionRecordFragment.instance(DelegationTransactionRecordFragment.TYPE_BOND)
            },
            object : FragmentCreator {
                override fun createFragment() =
                    DelegationTransactionRecordFragment.instance(DelegationTransactionRecordFragment.TYPE_UN_BOND)
            },
            object : FragmentCreator {
                override fun createFragment() =
                    DelegationTransactionRecordFragment.instance(DelegationTransactionRecordFragment.TYPE_REDELEGATE)
            }
        )
    }
}