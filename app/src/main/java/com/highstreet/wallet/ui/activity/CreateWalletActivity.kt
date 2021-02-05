package com.highstreet.wallet.ui.activity

import android.app.Activity
import androidx.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.highstreet.lib.common.AppManager
import com.highstreet.lib.extensions.string
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.constant.Colors
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.extensions.isName
import com.highstreet.wallet.model.WalletParams
import com.highstreet.wallet.ui.vm.CreateWalletVM
import kotlinx.android.synthetic.main.g_activity_create_wallet.*

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */
class CreateWalletActivity : BaseActivity() {

    private lateinit var walletParams: WalletParams
    private var chain = ""

    private val viewModel by lazy {
        ViewModelProvider(this).get(CreateWalletVM::class.java)
    }

    override fun getLayoutId() = R.layout.g_activity_create_wallet

    override fun prepare(savedInstanceState: Bundle?) {
        chain = intent.getStringExtra(ExtraKey.STRING) ?: ""
    }

    override fun initView() {
        setTitle(R.string.createWallet)

        etName.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus -> nameLine.setBackgroundColor(if (hasFocus) Colors.editLineFocus else Colors.editLineBlur) }

        RxView.textChanges(etName) {
            btnCreate.isEnabled = etName.string().isNotEmpty()
        }

        RxView.click(btnCreate) {
            createWallet()
        }
    }

    override fun initData() {
        walletParams = WalletParams.create()
        etAddress.setText(walletParams.address)
        viewModel.resultLD.observe(this, Observer {
            hideLoading()
            if (true == it) {
                AppManager.instance().finishActivity(InitWalletActivity::class.java)
                val account = AccountManager.instance().account!!
                BackupActivity.start(
                    this,
                    BackupActivity.FROM_CREATE,
                    account,
                    walletParams.mnemonic as ArrayList<String>
                )
                finish()
            } else {
                toast(R.string.failed)
            }
        })
    }

    private fun createWallet() {
        if (null == AccountManager.instance().password) {
            toast(R.string.setPassword)
            CreatePasswordActivity.start(this)
            return
        }

        val name = etName.string()

        if (!name.isName()) {
            toast(R.string.walletNameFormatError)
            return
        }
        walletParams.nickName = name

        showLoading()
        viewModel.createWallet(walletParams)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CreatePasswordActivity.REQUEST_CODE_CREATE_PASSWORD && resultCode == Activity.RESULT_OK) {
            createWallet()
        }
    }

    companion object {
        fun start(context: Context, chain: String, isAdd: Boolean) {
            val intent = Intent(context, CreateWalletActivity::class.java)
            intent.putExtra(ExtraKey.STRING, chain)
            intent.putExtra(ExtraKey.BOOLEAN, isAdd)
            context.startActivity(intent)
        }
    }
}