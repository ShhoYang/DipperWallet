package com.highstreet.wallet.ui.activity

import android.content.Context
import android.content.Intent
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.databinding.ActivityWalletTypeBinding
import com.highstreet.wallet.view.listener.RxView

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */
@AndroidEntryPoint(injectViewModel = false)
class WalletTypeActivity : BaseActivity<ActivityWalletTypeBinding, PlaceholderViewModel>() {

    override fun initView() {
        setTitle(R.string.walletType)
        viewBinding {
            RxView.click(clDip) {
                val toDoType =
                    intent.getIntExtra(ExtraKey.INT, InitWalletActivity.TO_DO_TYPE_CREATE)
                val isAdd = intent.getBooleanExtra(ExtraKey.BOOLEAN, false)

                if (InitWalletActivity.TO_DO_TYPE_CREATE == toDoType) {
                    CreateWalletActivity.start(this@WalletTypeActivity, "", isAdd)
                    finish()
                } else {
                    ImportWalletActivity.start(this@WalletTypeActivity, "", isAdd)
                    finish()
                }
            }
        }
    }

    override fun initData() {
    }

    companion object {

        fun start(context: Context, toDoType: Int, isAdd: Boolean = false) {
            val intent = Intent(context, WalletTypeActivity::class.java)
            intent.putExtra(ExtraKey.INT, toDoType)
            intent.putExtra(ExtraKey.BOOLEAN, isAdd)
            context.startActivity(intent)
        }
    }
}