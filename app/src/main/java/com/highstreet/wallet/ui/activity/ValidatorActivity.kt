package com.highstreet.wallet.ui.activity

import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.wallet.R
import com.highstreet.wallet.ui.adapter.FragmentWithTabAdapter
import com.highstreet.wallet.constant.ValidatorType
import com.highstreet.wallet.ui.fragment.ValidatorFragment
import kotlinx.android.synthetic.main.g_activity_viewpager.*

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
class ValidatorActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.g_activity_viewpager

    override fun initView() {
        setTitle(R.string.validator)
        val fragments = arrayListOf<Pair<String, Fragment>>(
            Pair(getString(R.string.bonded), ValidatorFragment.instance(ValidatorType.BONDED)),
            Pair(getString(R.string.jailed), ValidatorFragment.instance(ValidatorType.JAILED)),
            Pair(getString(R.string.allValidator), ValidatorFragment.instance(ValidatorType.ALL))
        )
        viewPager.adapter = FragmentWithTabAdapter(supportFragmentManager, lifecycle, fragments)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if (position in 0 until fragments.size) {
                tab.text = fragments[position].first
            }
        }.attach()
    }
}