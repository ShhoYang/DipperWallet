package com.highstreet.wallet.ui.activity

import android.content.Context
import android.content.Intent
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.annotation.Inject
import com.hao.library.extensions.init
import com.hao.library.ui.BaseActivity
import com.hao.library.view.listener.RxView
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.databinding.ActivityBackupBinding
import com.highstreet.wallet.db.Account
import com.highstreet.wallet.ui.adapter.MnemonicAdapter

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */
@AndroidEntryPoint(injectViewModel = false)
class BackupActivity : BaseActivity<ActivityBackupBinding, PlaceholderViewModel>() {

    private var from = FROM_CREATE

    @Inject
    lateinit var adapter: MnemonicAdapter

    override fun initView() {
        setTitle(R.string.ba_backupMnemonic)
    }

    override fun initData() {
        from = intent.getIntExtra(ExtraKey.INT, FROM_CREATE)
        val account = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as Account?
        val mnemonic = intent.getSerializableExtra(ExtraKey.SERIALIZABLE_2) as ArrayList<String>?
        if (account == null || mnemonic == null || mnemonic.isEmpty()) {
            finish()
            return
        }
        viewBinding {
            rv.init(adapter, 4)
            adapter.resetData(mnemonic)
            RxView.click(btnNext) {
                BackupVerifyActivity.start(this@BackupActivity, from, account, mnemonic)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        if (FROM_CREATE == from) {
            toA(MainActivity::class.java, true)
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