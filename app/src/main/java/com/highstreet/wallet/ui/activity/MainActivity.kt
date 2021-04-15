package com.highstreet.wallet.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.hao.library.view.listener.RxView
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.R
import com.highstreet.wallet.cache.CacheManager
import com.highstreet.wallet.databinding.ActivityMainBinding
import com.highstreet.wallet.db.Db
import com.highstreet.wallet.ui.adapter.FragmentAdapter
import com.highstreet.wallet.ui.fragment.*
import com.highstreet.wallet.ui.vm.NodeInfoVM

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, NodeInfoVM>() {

    override fun initView() {
        val fragments = arrayListOf<Pair<String, Fragment>>(
            Pair("", WalletFragment()),
            Pair("", TokensFragment()),
            Pair("", HistoryFragment()),
//            Pair("", DAppFragment()),
            Pair("", SettingFragment())
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
                        R.id.tabTokens -> 1
                        R.id.tabHistory -> 2
//                        R.id.tabDapp -> 3
                        R.id.tabSetting -> 3
                        else -> 0
                    }, false
                )

                true
            }

            RxView.click(ivSwitchWallet, WalletManageActivity::class.java, this@MainActivity::toA)
        }
    }

    override fun onStart() {
        super.onStart()
        CacheManager.load()
    }

    override fun initData() {
        Db.instance().accountDao().queryFirstUserAsLiveData().observe(this, {
            it?.apply {
                AccountManager.instance().changeCurrentAccount(it)
                vm?.getNodeInfo()
                viewBinding {
                    tvWalletName.text = nickName
                }
            }
        })

        viewModel {
            nodeInfoLD.observe(this@MainActivity) {
                vb?.tvChainName?.text = it?.node_info?.network
            }

            getNodeInfo()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
    }
}
