package com.highstreet.wallet.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.highstreet.lib.common.AppManager
import com.highstreet.lib.extensions.visibility
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.constant.ExtraKey
import kotlinx.android.synthetic.main.g_activity_init_wallet.*


/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */
class InitWalletActivity : BaseActivity() {

    private var isAdd = false

    override fun prepare(savedInstanceState: Bundle?) {
        isAdd = intent.getBooleanExtra(ExtraKey.BOOLEAN, false)
    }

    override fun showToolbar() = false

    override fun getLayoutId() = R.layout.g_activity_init_wallet

    override fun initView() {
        getToolbar()?.visibility(isAdd)
        RxView.click(btnCreate) {
            CreateWalletActivity.start(this, "", isAdd)
        }

        RxView.click(btnImport) {
            ImportWalletActivity.start(this, "", isAdd)
        }
    }

    override fun initData() {
        if (AccountManager.instance().accounts.isEmpty()) {
            AppManager.instance().finishAllActivityExceptAppoint(this)
        }
    }

    companion object {

        /**
         * 怎么去创建
         */
        const val TO_DO_TYPE_CREATE = 1
        const val TO_DO_TYPE_IMPORT = 2

        fun start(context: Context, isAdd: Boolean = false) {
            val intent = Intent(context, InitWalletActivity::class.java)
            intent.putExtra(ExtraKey.BOOLEAN, isAdd)
            context.startActivity(intent)
        }
    }
}