package com.highstreet.wallet.ui.activity

import android.Manifest
import android.os.Bundle
import com.hao.library.AppManager
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.hao.library.utils.CoroutineUtils
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.App
import com.highstreet.wallet.databinding.ActivityWelcomeBinding
import com.highstreet.wallet.db.Account
import com.tbruyelle.rxpermissions2.RxPermissions

@AndroidEntryPoint(injectViewModel = false)
class WelcomeActivity : BaseActivity<ActivityWelcomeBinding, PlaceholderViewModel>() {

    override fun initView() {
        AppManager.instance().finishAllActivityExceptAppoint(this)
        CoroutineUtils.io2main({
            App.instance.getOldDB()
            Thread.sleep(2000)
            val a = AccountManager.instance().init()
            a
        }, {
            requestPermissions(it)
        })
    }

    private fun requestPermissions(account: Account?) {
        RxPermissions(this).request(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
        ).subscribe {
            if (it) {
                if (account?.address == null || account.address.isEmpty()) {
                    InitWalletActivity.start(this)
                    finish()
                } else {
                    toA(MainActivity::class.java, true)
                }
            } else {
                AppManager.instance().exit()
            }
        }
    }

    override fun initData() {
    }

    override fun onSaveInstanceState(outState: Bundle) {
    }
}
