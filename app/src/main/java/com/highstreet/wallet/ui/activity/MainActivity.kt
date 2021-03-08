package com.highstreet.wallet.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.R
import com.highstreet.wallet.databinding.ActivityMainBinding
import com.highstreet.wallet.db.Db
import com.highstreet.wallet.ui.adapter.FragmentAdapter
import com.highstreet.wallet.ui.fragment.*
import com.highstreet.wallet.view.listener.RxView

@AndroidEntryPoint(injectViewModel = false)
class MainActivity : BaseActivity<ActivityMainBinding, PlaceholderViewModel>() {

    override fun initView() {
        val fragments = arrayListOf<Pair<String, Fragment>>(
            Pair("", WalletFragment()),
            Pair("", TokensFragment()),
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

           RxView.click(ivSwitchWallet){
               toA(WalletManageActivity::class.java)
           }
        }
    }

    override fun initData() {
        Db.instance().accountDao().queryLastUserAsLiveData(true).observe(this, {
            it?.apply {
                viewBinding {
                    tvWalletName.text = nickName
                    tvChainName.text = "(${chain})"
                }
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
    }
}
