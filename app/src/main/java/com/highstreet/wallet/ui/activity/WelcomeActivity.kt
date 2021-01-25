package com.highstreet.wallet.ui.activity

import android.Manifest
import android.os.Bundle
import com.highstreet.lib.common.AppManager
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.lib.utils.CoroutineUtils
import com.highstreet.wallet.R
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.db.Account
import com.tbruyelle.rxpermissions2.RxPermissions

class WelcomeActivity : BaseActivity() {

    override fun showToolbar() = false

    override fun getLayoutId() = R.layout.g_activity_welcome

    override fun initView() {
        AppManager.instance().finishAllActivityExceptAppoint(this)
        CoroutineUtils.io2main({
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
                    to(MainActivity::class.java, true)
                }
            } else {
                AppManager.instance().exit()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
    }
}
