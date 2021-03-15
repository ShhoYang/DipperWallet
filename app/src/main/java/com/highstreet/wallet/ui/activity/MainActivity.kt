package com.highstreet.wallet.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.highstreet.wallet.R
import com.highstreet.wallet.databinding.ActivityMainBinding
import com.highstreet.wallet.db.Db
import com.highstreet.wallet.ui.adapter.FragmentAdapter
import com.highstreet.wallet.ui.fragment.*
import com.highstreet.wallet.ui.vm.NodeInfoVM
import com.highstreet.wallet.view.listener.RxView

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, NodeInfoVM>() {

    override fun initView() {
        val fragments = arrayListOf<Pair<String, Fragment>>(
            Pair("", WalletFragment()),
            Pair("", MyTokenFragment()),
            Pair("", HistoryFragment()),
            Pair("", DAppFragment()),
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
                        R.id.tabDapp -> 3
                        R.id.tabSetting -> 4
                        else -> 0
                    }, false
                )

                true
            }

            RxView.click(ivSwitchWallet) {
                toA(WalletManageActivity::class.java)
            }
        }
    }

    override fun initData() {
        Db.instance().accountDao().queryFirstUserAsLiveData().observe(this, {
            it?.apply {
                viewBinding {
                    tvWalletName.text = nickName
                }
            }
        })

        viewModel {
            nodeInfoLD.observe(this@MainActivity) {
                vb?.tvChainName?.text = "(${it?.node_info?.network})"
            }

            getNodeInfo()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
    }
}
