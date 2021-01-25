package com.highstreet.wallet.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @author Yang Shihao
 * @date 2020/10/20
 */
class FragmentWithTabAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle,
    private var fragments: List<Pair<String, Fragment>>
) :
    FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position].second
    }
}
