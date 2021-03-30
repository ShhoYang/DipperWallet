package com.highstreet.wallet.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.KeyEvent
import android.widget.EditText
import com.hao.library.AppManager
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.hao.library.view.listener.RxView
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.Chain
import com.highstreet.wallet.constant.Constant
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.crypto.KeyUtils
import com.highstreet.wallet.databinding.ActivityImportWalletBinding
import com.highstreet.wallet.extensions.focusListener
import com.highstreet.wallet.extensions.isName
import com.highstreet.wallet.extensions.string
import com.highstreet.wallet.model.WalletParams
import com.highstreet.wallet.ui.vm.ImportWalletVM

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */
@AndroidEntryPoint
class ImportWalletActivity : BaseActivity<ActivityImportWalletBinding, ImportWalletVM>() {

    private var chain = ""

    private var focusPosition = 0

    private val editTexts = ArrayList<EditText>(25)

    override fun initView() {
        setTitle(R.string.iwa_importWallet)

        viewBinding {
            etName.focusListener(nameLine.line)

            editTexts.clear()
            editTexts.add(etMnemonic01)
            editTexts.add(etMnemonic02)
            editTexts.add(etMnemonic03)
            editTexts.add(etMnemonic04)
            editTexts.add(etMnemonic05)
            editTexts.add(etMnemonic06)
            editTexts.add(etMnemonic07)
            editTexts.add(etMnemonic08)
            editTexts.add(etMnemonic09)
            editTexts.add(etMnemonic10)
            editTexts.add(etMnemonic11)
            editTexts.add(etMnemonic12)
            editTexts.add(etMnemonic13)
            editTexts.add(etMnemonic14)
            editTexts.add(etMnemonic15)
            editTexts.add(etMnemonic16)
            editTexts.add(etMnemonic17)
            editTexts.add(etMnemonic18)
            editTexts.add(etMnemonic19)
            editTexts.add(etMnemonic20)
            editTexts.add(etMnemonic21)
            editTexts.add(etMnemonic22)
            editTexts.add(etMnemonic23)
            editTexts.add(etMnemonic24)

            editTexts.forEachIndexed { i, editText ->
                editText.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        focusPosition = i
                    }
                }
            }

            RxView.click(btnImport) {
                importWallet()
            }
        }
    }

    override fun initData() {
        chain = intent?.getStringExtra(ExtraKey.STRING) ?: Chain.DIP_TEST.chainName
        vm?.resultLD?.observe(this) {
            hideLoading()
            if (true == it) {
                AppManager.instance().finishActivity(InitWalletActivity::class.java)
                if (!intent.getBooleanExtra(ExtraKey.BOOLEAN, false)) {
                    toA(MainActivity::class.java)
                }
                finish()
            } else {
                toast(R.string.failed)
            }
        }
    }

    private fun importWallet() {
        if (null == AccountManager.instance().password) {
            toast(R.string.iwa_setPassword)
            CreatePasswordActivity.start(this)
            return
        }

        editTexts.forEach {
            if (TextUtils.isEmpty(it.string())) {
                toast(R.string.iwa_mnemonicNotCompleted)
                return
            }
        }

        val mnemonic = editTexts.map { it.string() } as ArrayList

        if (!isValidMnemonic(mnemonic)) {
            toast(R.string.iwa_invalidMnemonic)
            return
        }

        val walletParams = WalletParams.import(chain, mnemonic)
        val accounts = AccountManager.instance().accounts
        accounts.forEach { account ->
            if (account.address == walletParams.address) {
                toast(R.string.iwa_addressAdded)
                return
            }
        }

        val name = vb?.etName?.string() ?: ""

        if (!name.isName()) {
            toast(R.string.iwa_walletNameFormatError)
            return
        }
        walletParams.nickName = name
        showLoading()
        vm?.importWallet(walletParams)
    }

    private fun isValidMnemonic(mnemonic: ArrayList<String>): Boolean {
        return mnemonic.size == Constant.MNEMONIC_SIZE && KeyUtils.isValidMnemonic(mnemonic)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CreatePasswordActivity.REQUEST_CODE_CREATE_PASSWORD && resultCode == Activity.RESULT_OK) {
            importWallet()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (focusPosition > 0) {
                val editText = editTexts[focusPosition - 1]
                editText.requestFocus()
                editText.setSelection(editText.string().length)
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        fun start(context: Context, chain: String, isAdd: Boolean) {
            val intent = Intent(context, ImportWalletActivity::class.java)
            intent.putExtra(ExtraKey.STRING, chain)
            intent.putExtra(ExtraKey.BOOLEAN, isAdd)
            context.startActivity(intent)
        }
    }
}