package com.highstreet.wallet.ui.activity

import android.os.Bundle
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.wallet.R
import com.highstreet.wallet.ui.adapter.ViewPagerAdapter
import com.highstreet.wallet.ui.fragment.CapitalFragment
import com.highstreet.wallet.ui.fragment.MeFragment
import com.highstreet.wallet.ui.fragment.StakingFragment
import com.highstreet.wallet.ui.fragment.ProposalFragment
import kotlinx.android.synthetic.main.g_activity_main.*

class MainActivity : BaseActivity() {

    override fun showToolbar() = false

    override fun getLayoutId() = R.layout.g_activity_main

    override fun initView() {
        val fragments = listOf(
                CapitalFragment(),
                StakingFragment(),
                ProposalFragment(),
                MeFragment()
        )
        viewPager.apply {
            offscreenPageLimit = 3
            adapter = ViewPagerAdapter(supportFragmentManager, fragments)
        }
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            viewPager.setCurrentItem(
                    when (item.itemId) {
                        R.id.tab_staking -> 1
                        R.id.tab_proposal -> 2
                        R.id.tab_me -> 3
                        else -> 0
                    }, false
            )

            true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
    }
}
