package com.highstreet.wallet.ui.activity

import android.content.Context
import android.content.Intent
import com.hao.library.AppManager
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.extensions.visibility
import com.hao.library.ui.BaseActivity
import com.hao.library.ui.UIParams
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.databinding.ActivityInitWalletBinding
import com.highstreet.wallet.view.listener.RxView

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */
@AndroidEntryPoint(injectViewModel = false)
class InitWalletActivity : BaseActivity<ActivityInitWalletBinding, PlaceholderViewModel>() {

    private var isAdd = false

    override fun prepare(uiParams: UIParams, intent: Intent?) {
        super.prepare(uiParams, intent)
        isAdd = intent?.getBooleanExtra(ExtraKey.BOOLEAN, false) ?: false
    }

    override fun initView() {
        toolbarLayout {
            visibility(isAdd)
        }
        viewBinding {
            RxView.click(btnCreate) {
                WalletTypeActivity.start(
                    this@InitWalletActivity,
                    WalletTypeActivity.TO_DO_TYPE_CREATE,
                    isAdd
                )
            }

            RxView.click(btnImport) {
                WalletTypeActivity.start(
                    this@InitWalletActivity,
                    WalletTypeActivity.TO_DO_TYPE_IMPORT,
                    isAdd
                )
            }
        }
    }

    override fun initData() {
        if (AccountManager.instance().accounts.isEmpty()) {
            AppManager.instance().finishAllActivityExceptAppoint(this)
        }
    }

    companion object {
        /**
         * @param isAdd true添加 false新建
         */
        fun start(context: Context, isAdd: Boolean = false) {
            val intent = Intent(context, InitWalletActivity::class.java)
            intent.putExtra(ExtraKey.BOOLEAN, isAdd)
            context.startActivity(intent)
        }
    }
}