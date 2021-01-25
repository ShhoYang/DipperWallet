package com.highstreet.wallet.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */
class ViewPagerAdapter(fm: FragmentManager, private val fragments: List<Fragment>) : FragmentPagerAdapter(fm) {

    override fun getCount() = fragments.size

    override fun getItem(position: Int) = fragments[position]

}