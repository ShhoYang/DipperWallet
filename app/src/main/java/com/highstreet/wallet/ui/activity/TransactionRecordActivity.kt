package com.highstreet.wallet.ui.activity

import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseViewPagerActivity
import com.hao.library.ui.FragmentCreator
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.R
import com.highstreet.wallet.databinding.ActivityViewpagerBinding
import com.highstreet.wallet.ui.fragment.TransactionRecordFragment

/**
 * @author Yang Shihao
 * @Date 2020/10/20
 */
@AndroidEntryPoint(injectViewModel = false)
class TransactionRecordActivity :
    BaseViewPagerActivity<ActivityViewpagerBinding, PlaceholderViewModel>() {

    override fun initView() {
        setTitle(R.string.transactionRecord)
        super.initView()
    }

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