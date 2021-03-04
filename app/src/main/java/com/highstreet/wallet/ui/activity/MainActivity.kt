package com.highstreet.wallet.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.R
import com.highstreet.wallet.databinding.ActivityMainBinding
import com.highstreet.wallet.ui.adapter.FragmentAdapter
import com.highstreet.wallet.ui.fragment.*

@AndroidEntryPoint(injectViewModel = false)
class MainActivity : BaseActivity<ActivityMainBinding, PlaceholderViewModel>() {

    override fun initView() {
        val fragments = arrayListOf<Pair<String, Fragment>>(
            Pair("", CapitalFragment()),
            Pair("", StakingFragment()),
            Pair("", ProposalFragment()),
            Pair("", BrowserFragment()),
            Pair("", MeFragment())
        )

        viewBinding {
            viewPager.apply {
                isUserInputEnabled = false
                offscreenPageLimit = 4
                adapter = FragmentAdapter(supportFragmentManager, lifecycle, fragments)
            }

            bottomNavigationView.setOnNavigationItemSelectedListener { item ->
                viewPager.setCurrentItem(
                    when (item.itemId) {
                        R.id.tab_staking -> 1
                        R.id.tab_proposal -> 2
                        R.id.tab_browser -> 3
                        R.id.tab_me -> 4
                        else -> 0
                    }, false
                )

                true
            }
        }
    }

    override fun initData() {
    }

    override fun onSaveInstanceState(outState: Bundle) {
    }
}
