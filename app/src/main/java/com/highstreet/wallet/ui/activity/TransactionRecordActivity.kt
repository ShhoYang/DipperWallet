package com.highstreet.wallet.ui.activity

import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.wallet.R
import com.highstreet.wallet.ui.adapter.FragmentWithTabAdapter
import com.highstreet.wallet.ui.fragment.TransactionRecordFragment
import kotlinx.android.synthetic.main.g_activity_viewpager.*


/**
 * @author Yang Shihao
 * @Date 2020/10/20
 */

class TransactionRecordActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.g_activity_viewpager

    override fun initView() {
        title = "交易明细"
        val fragments = arrayListOf<Pair<String, Fragment>>(
                Pair("转入", TransactionRecordFragment.instance(true)),
                Pair("转出", TransactionRecordFragment.instance(false))
        )
        viewPager.adapter = FragmentWithTabAdapter(supportFragmentManager,lifecycle, fragments)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if (position in 0 until fragments.size) {
                tab.text = fragments[position].first
            }
        }.attach()
    }
}