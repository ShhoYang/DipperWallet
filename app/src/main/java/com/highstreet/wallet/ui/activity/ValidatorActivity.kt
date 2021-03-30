package com.highstreet.wallet.ui.activity

import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseViewPagerActivity
import com.hao.library.ui.FragmentCreator
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.R
import com.highstreet.wallet.databinding.ActivityViewpagerBinding
import com.highstreet.wallet.ui.fragment.AllValidatorFragment
import com.highstreet.wallet.ui.fragment.MyValidatorFragment

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
@AndroidEntryPoint
class ValidatorActivity :
    BaseViewPagerActivity<ActivityViewpagerBinding, PlaceholderViewModel>() {

    override fun initView() {
        super.initView()
        setTitle(R.string.va_validator)
    }

    override fun getTitles(): List<String> {
        return arrayListOf(
            getString(R.string.va_my),
            getString(R.string.va_allValidator),
        )
    }

    override fun getFragments(): List<FragmentCreator> {
        return arrayListOf(
            object : FragmentCreator {
                override fun createFragment() = MyValidatorFragment()
            },
            object : FragmentCreator {
                override fun createFragment() = AllValidatorFragment()
            }
        )
    }
}