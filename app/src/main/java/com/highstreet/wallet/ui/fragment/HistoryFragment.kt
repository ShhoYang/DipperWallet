package com.highstreet.wallet.ui.fragment

import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseViewPagerFragment
import com.hao.library.ui.FragmentCreator
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.R
import com.highstreet.wallet.databinding.FragmentViewpagerBinding

/**
 * @author Yang Shihao
 * @Date 3/6/21
 */
@AndroidEntryPoint(injectViewModel = false)
class HistoryFragment : BaseViewPagerFragment<FragmentViewpagerBinding, PlaceholderViewModel>() {

    override fun getTitles(): List<String> {
        return arrayListOf(
            getString(R.string.transferIn),
            getString(R.string.transferOut)
        )
    }

    override fun getFragments(): List<FragmentCreator> {
        return arrayListOf(
            object : FragmentCreator {
                override fun createFragment() = TransactionRecordFragment.instance(true)
            },
            object : FragmentCreator {
                override fun createFragment() = TransactionRecordFragment.instance(false)
            }
        )
    }
}