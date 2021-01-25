package com.highstreet.wallet.ui.activity

import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.wallet.R
import com.highstreet.wallet.ui.adapter.FragmentWithTabAdapter
import com.highstreet.wallet.ui.fragment.DelegationTransactionRecordFragment
import kotlinx.android.synthetic.main.g_activity_viewpager.*

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

class DelegationTransactionRecordActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.g_activity_viewpager

    override fun initView() {
        title = "委托交易记录"
        val fragments = arrayListOf<Pair<String, Fragment>>(
            Pair(
                "委托",
                DelegationTransactionRecordFragment.instance(DelegationTransactionRecordFragment.TYPE_BOND)
            ),
            Pair(
                "解委托",
                DelegationTransactionRecordFragment.instance(DelegationTransactionRecordFragment.TYPE_UN_BOND)
            ),
            Pair(
                "重新委托",
                DelegationTransactionRecordFragment.instance(DelegationTransactionRecordFragment.TYPE_REDELEGATE)
            )
        )
        viewPager.adapter = FragmentWithTabAdapter(supportFragmentManager, lifecycle, fragments)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if (position in 0 until fragments.size) {
                tab.text = fragments[position].first
            }
        }.attach()
    }
}