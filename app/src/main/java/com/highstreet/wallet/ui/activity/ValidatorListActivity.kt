package com.highstreet.wallet.ui.activity

import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseViewPagerActivity
import com.hao.library.ui.FragmentCreator
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.R
import com.highstreet.wallet.databinding.ActivityViewpagerBinding
import com.highstreet.wallet.ui.fragment.AllValidatorListFragment
import com.highstreet.wallet.ui.fragment.MyValidatorListFragment

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
@AndroidEntryPoint
class ValidatorListActivity :
    BaseViewPagerActivity<ActivityViewpagerBinding, PlaceholderViewModel>() {

    override fun initView() {
        super.initView()
        setTitle(R.string.validator)
    }

    override fun getTitles(): List<String> {
        return arrayListOf(
            getString(R.string.my),
            getString(R.string.allValidator),
        )
    }

    override fun getFragments(): List<FragmentCreator> {
        return arrayListOf(
            object : FragmentCreator {
                override fun createFragment() = MyValidatorListFragment()
            },
            object : FragmentCreator {
                override fun createFragment() = AllValidatorListFragment()
            }
        )
    }
}