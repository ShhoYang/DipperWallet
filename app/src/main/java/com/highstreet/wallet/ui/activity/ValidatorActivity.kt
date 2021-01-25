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
        title = "验证人"
        val fragments = arrayListOf<Pair<String, Fragment>>(
                Pair("共识中", ValidatorFragment.instance(ValidatorType.BONDED)),
//                Pair("候选中", ValidatorFragment.instance(ValidatorType.UN_BONDING)),
                Pair("待解禁", ValidatorFragment.instance(ValidatorType.JAILED)),
                Pair("全部", ValidatorFragment.instance(ValidatorType.ALL))
        )
        viewPager.adapter = FragmentWithTabAdapter(supportFragmentManager, lifecycle,fragments)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if (position in 0 until fragments.size) {
                tab.text = fragments[position].first
            }
        }.attach()
    }
}