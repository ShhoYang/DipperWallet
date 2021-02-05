package com.highstreet.wallet.ui.fragment

import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.highstreet.lib.ui.BaseFragment
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.ui.activity.DelegationTransactionRecordActivity
import com.highstreet.wallet.ui.adapter.FragmentWithTabAdapter
import kotlinx.android.synthetic.main.g_fragment_staking.*


/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

class StakingFragment : BaseFragment() {

    override fun getLayoutId() = R.layout.g_fragment_staking

    override fun initView() {
        val fragments = arrayListOf<Pair<String, Fragment>>(
            Pair(getString(R.string.bond), DelegationListFragment()),
            Pair(getString(R.string.unbond), UndelegationListFragment())
        )
        viewPager.adapter = FragmentWithTabAdapter(childFragmentManager, lifecycle, fragments)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if (position in 0 until fragments.size) {
                tab.text = fragments[position].first
            }
        }.attach()
        RxView.click(ivRecord) {
            to(DelegationTransactionRecordActivity::class.java)
        }
    }
}