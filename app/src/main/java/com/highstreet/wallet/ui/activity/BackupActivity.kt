package com.highstreet.wallet.ui.activity

import android.content.Context
import android.content.Intent
import com.highstreet.lib.extensions.init
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.db.Account
import com.highstreet.wallet.ui.adapter.MnemonicAdapter
import kotlinx.android.synthetic.main.g_activity_backup.*

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */
class BackupActivity : BaseActivity() {

    private var from = FROM_CREATE
    private var account: Account? = null
    private var mnemonic: ArrayList<String>? = null

    override fun getLayoutId() = R.layout.g_activity_backup

    override fun initView() {
        title = "立即备份"
    }

    override fun initData() {
        from = intent.getIntExtra(ExtraKey.INT, FROM_CREATE)
        account = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as Account?
        mnemonic = intent.getSerializableExtra(ExtraKey.SERIALIZABLE_2) as ArrayList<String>?
        if (account == null || mnemonic == null || mnemonic!!.isEmpty()) {
            finish()
            return
        }
        rv.init(MnemonicAdapter(mnemonic!!), 4)
        RxView.click(btnNext) {
            BackupVerifyActivity.start(this, from, account!!, mnemonic!!)
            finish()
        }
    }

    override fun onBackPressed() {
        if (FROM_CREATE == from) {
            to(MainActivity::class.java, true)
        } else {
            finish()
        }
    }

    companion object {

        const val FROM_CREATE = 1
        const val FROM_WALLET_MANAGER = 2

        fun start(context: Context, from: Int, account: Account, mnemonic: ArrayList<String>) {
            val intent = Intent(context, BackupActivity::class.java)
            intent.putExtra(ExtraKey.INT, from)
            intent.putExtra(ExtraKey.SERIALIZABLE, account)
            intent.putExtra(ExtraKey.SERIALIZABLE_2, mnemonic)
            context.startActivity(intent)
        }
    }
}