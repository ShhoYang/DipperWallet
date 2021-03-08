package com.highstreet.wallet.ui.fragment

import android.view.View
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseNormalListFragment
import com.hao.library.utils.AppUtils
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.App
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.Constant
import com.highstreet.wallet.databinding.FragmentSettingBinding
import com.highstreet.wallet.db.Db
import com.highstreet.wallet.model.Menu
import com.highstreet.wallet.ui.activity.LockActivity
import com.highstreet.wallet.ui.activity.WalletManageActivity
import com.highstreet.wallet.ui.activity.WebActivity
import com.highstreet.wallet.ui.adapter.SettingAdapter

/**
 * @author Yang Shihao
 * @Date 3/6/21
 */
@AndroidEntryPoint(injectViewModel = false)
class SettingFragment :
    BaseNormalListFragment<FragmentSettingBinding, Menu, PlaceholderViewModel, SettingAdapter>() {
    override fun initData() {
        Db.instance().passwordDao().queryByIdAsLiveData(Constant.PASSWORD_DEFAULT_ID)
            .observe(this) {
                initMenu()
            }

        initMenu()
    }

    private fun initMenu() {
        val list = ArrayList<Menu>()
        list.add(Menu.group(getString(R.string.wallet)))
        list.add(
            Menu(
                title = getString(R.string.walletManager),
                action = WalletManageActivity::class.java
            )
        )
        list.add(Menu.space())
        list.add(Menu.group(getString(R.string.general)))
        list.add(
            Menu(
                title = getString(R.string.appLock),
                desc = if (AccountManager.instance().fingerprint) getString(R.string.open) else getString(
                    R.string.close
                ),
                action = LockActivity::class.java
            )
        )
        list.add(Menu.space())
        list.add(
            Menu(
                title = getString(R.string.currency),
                desc = getString(R.string.cny)
            )
        )
        list.add(Menu.space())
        list.add(
            Menu(
                title = getString(R.string.basePrice),
                desc = getString(R.string.gecko)
            )
        )
        list.add(Menu.space())
        list.add(Menu.group(getString(R.string.support)))
        list.add(
            Menu(
                title = getString(R.string.faq),
                data = Constant.DIPPER_NETWORK,
                action = WebActivity::class.java
            )
        )
        list.add(Menu.space())
        list.add(
            Menu(
                title = getString(R.string.telegram),
                data = Constant.DIPPER_NETWORK,
                action = WebActivity::class.java
            )
        )
        list.add(Menu.space())
        list.add(
            Menu(
                title = getString(R.string.explorer),
                data = Constant.DIPPER_NETWORK,
                action = WebActivity::class.java
            )
        )
        list.add(Menu.space())
        list.add(
            Menu(
                title = getString(R.string.dipperNetwork),
                data = Constant.DIPPER_NETWORK,
                action = WebActivity::class.java
            )
        )
        list.add(Menu.space())
        list.add(Menu.group(getString(R.string.appInfo)))
        list.add(
            Menu(
                title = getString(R.string.terms),
                data = Constant.DIPPER_NETWORK,
                action = WebActivity::class.java
            )
        )
        list.add(Menu.space())
        list.add(
            Menu(
                title = getString(R.string.openSource),
                data = Constant.DIPPER_NETWORK,
                action = WebActivity::class.java
            )
        )
        list.add(Menu.space())
        list.add(
            Menu(
                title = getString(R.string.version),
                desc = "v${AppUtils.getVersionName(App.instance)}"
            )
        )
        list.add(Menu.space())
        list.add(Menu.space())

        adapter.resetData(list)
    }

    override fun itemClicked(view: View, item: Menu, position: Int) {
        if (item.action != null) {
            if (item.action?.name == WebActivity::class.java.name) {
                act {
                    WebActivity.start(it, item.title, item.data ?: "")
                }
            } else {
                toA(item.action)
            }
        }
    }
}