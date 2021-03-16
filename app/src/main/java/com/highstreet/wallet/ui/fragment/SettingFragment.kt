package com.highstreet.wallet.ui.fragment

import android.view.View
import com.hao.library.adapter.listener.OnItemClickListener
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseNormalListFragment
import com.hao.library.utils.AppUtils
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.App
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.Constant
import com.highstreet.wallet.constant.Currency
import com.highstreet.wallet.databinding.FragmentListBinding
import com.highstreet.wallet.db.Db
import com.highstreet.wallet.model.Menu
import com.highstreet.wallet.ui.activity.*
import com.highstreet.wallet.ui.adapter.CenterMenuDialogAdapter
import com.highstreet.wallet.ui.adapter.SettingAdapter
import com.highstreet.wallet.view.CenterMenuDialog

/**
 * @author Yang Shihao
 * @Date 3/6/21
 */
@AndroidEntryPoint(injectViewModel = false)
class SettingFragment :
    BaseNormalListFragment<FragmentListBinding, Menu, PlaceholderViewModel, SettingAdapter>() {


    private var centerMenuDialogAdapter: CenterMenuDialogAdapter? = null

    private var centerMenuDialog: CenterMenuDialog? = null

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
                title = getString(R.string.walletManage),
                action = ACTION_WALLET_MANGER
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
                action = ACTION_APP_LOCK
            )
        )
        list.add(Menu.space())
        list.add(
            Menu(
                title = getString(R.string.currency),
                desc = AccountManager.instance().currencyType,
                action = ACTION_CURRENCY
            )
        )
        list.add(Menu.space())
        list.add(
            Menu(
                title = getString(R.string.basePrice),
                desc = getString(R.string.gecko),
                action = ACTION_BASE_PRICE
            )
        )
        list.add(Menu.space())
        list.add(Menu.group(getString(R.string.support)))
        list.add(
            Menu(
                title = getString(R.string.faq),
                action = ACTION_FAQ
            )
        )
        list.add(Menu.space())
        list.add(
            Menu(
                title = getString(R.string.telegram),
                action = ACTION_TELEGRAM
            )
        )
        list.add(Menu.space())
        list.add(
            Menu(
                title = getString(R.string.explorer),
                action = ACTION_EXPLORER
            )
        )
        list.add(Menu.space())
        list.add(
            Menu(
                title = getString(R.string.dipperNetwork),
                action = ACTION_DIPPER_NETWORK
            )
        )
        list.add(Menu.space())
        list.add(Menu.group(getString(R.string.appInfo)))
        list.add(
            Menu(
                title = getString(R.string.terms),
                action = ACTION_TEAMS
            )
        )
        list.add(Menu.space())
        list.add(
            Menu(
                title = getString(R.string.openSource),
                action = ACTION_OPEN_SOURCE
            )
        )
        list.add(Menu.space())
        list.add(
            Menu(
                title = getString(R.string.version),
                desc = "v${AppUtils.getVersionName(App.instance)}",
                action = ACTION_OPEN_VERSION
            )
        )
        list.add(Menu.space())
        list.add(Menu.space())

        adapter.resetData(list)
    }

    private fun showCurrency() {
        act {
            if (centerMenuDialog == null) {
                centerMenuDialogAdapter = CenterMenuDialogAdapter()
                centerMenuDialogAdapter!!.setOnItemClickListener(object :
                    OnItemClickListener<String> {
                    override fun itemClicked(view: View, item: String, position: Int) {
                        AccountManager.instance().refreshCurrency(item)
                        initMenu()
                        centerMenuDialog?.dismiss()
                    }
                })

                val optionList = ArrayList<String>()
                Currency.values().forEach { c ->
                    optionList.add(c.type)
                }

                centerMenuDialogAdapter!!.resetData(optionList)
                centerMenuDialog = CenterMenuDialog(it).setAdapter(centerMenuDialogAdapter!!)
            }

            centerMenuDialog!!.show()
        }
    }

    override fun itemClicked(view: View, item: Menu, position: Int) {
        when (item.action) {
            ACTION_WALLET_MANGER -> toA(WalletManageActivity::class.java)
            ACTION_APP_LOCK -> toA(LockActivity::class.java)
            ACTION_CURRENCY -> showCurrency()
            ACTION_BASE_PRICE, ACTION_FAQ, ACTION_TELEGRAM, ACTION_EXPLORER, ACTION_DIPPER_NETWORK, ACTION_TEAMS, ACTION_OPEN_SOURCE -> act {
                WebActivity.start(it, item.title, Constant.DIPPER_NETWORK)
            }
        }
    }

    companion object {
        private const val ACTION_WALLET_MANGER = 0
        private const val ACTION_APP_LOCK = 1
        private const val ACTION_CURRENCY = 2
        private const val ACTION_BASE_PRICE = 3
        private const val ACTION_FAQ = 4
        private const val ACTION_TELEGRAM = 5
        private const val ACTION_EXPLORER = 6
        private const val ACTION_DIPPER_NETWORK = 7
        private const val ACTION_TEAMS = 8
        private const val ACTION_OPEN_SOURCE = 9
        private const val ACTION_OPEN_VERSION = 10
    }
}