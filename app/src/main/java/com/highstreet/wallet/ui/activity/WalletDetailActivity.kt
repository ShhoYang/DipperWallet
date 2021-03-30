package com.highstreet.wallet.ui.activity

import android.content.Context
import android.content.Intent
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.hao.library.utils.DateUtils
import com.hao.library.view.listener.RxView
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.Constant
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.crypto.KeyUtils
import com.highstreet.wallet.databinding.ActivityWalletDetailBinding
import com.highstreet.wallet.db.Account
import com.highstreet.wallet.fingerprint.FingerprintUtils
import com.highstreet.wallet.ui.vm.WalletDetailVM
import com.highstreet.wallet.view.InputDialog
import com.highstreet.wallet.view.InputDialogListener
import com.highstreet.wallet.view.QRDialog

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */
@AndroidEntryPoint
class WalletDetailActivity :
    BaseActivity<ActivityWalletDetailBinding, WalletDetailVM>() {

    private var tempNickname = ""
    private var account: Account? = null

    private var type = 0

    override fun initView() {
        setTitle(R.string.wda_walletManage)
        viewBinding {
            RxView.click(ivEditName, editName)
            RxView.click(ivWalletAddress, showQr)
            RxView.click(btnBackup, TYPE_BACKUP, fingerprint)
            RxView.click(btnDelete, TYPE_DELETE, fingerprint)
        }
    }

    override fun initData() {
        account = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as Account?
        if (account == null) {
            finish()
            return
        }
        viewBinding {
            account?.let {
                ivChainIcon.setImageResource(it.getIcon())
                tvWalletName.text = it.nickName
                tvWalletAddress.text = it.address
                tvChainName.text = it.chain
                tvImportTime.text = DateUtils.stamp2Str(it.createTime)
                tvPath.text = Constant.WALLET_PATH
            }
        }
        viewModel {
            updateNameLD.observe(this@WalletDetailActivity) {
                hideLoading()
                if (it) {
                    toast(R.string.wda_updateSucceed)
                    account?.nickName = tempNickname
                    vb?.tvWalletName?.text = tempNickname
                } else {
                    toast(R.string.wda_updateFailed)
                }
            }
            deleteLD.observe(this@WalletDetailActivity) {
                hideLoading()
                if (it) {
                    toast(R.string.wda_deleteSucceed)
                    if (AccountManager.instance().accounts.isEmpty()) {
                        InitWalletActivity.start(this@WalletDetailActivity)
                    } else {
                        finish()
                    }
                } else {
                    toast(R.string.wda_deleteFailed)
                }
            }
        }
    }

    private val editName = {
        InputDialog(this)
            .setTitle(getString(R.string.wda_updateWalletName))
            .setHint(getString(R.string.wda_inputNewWalletName))
            .setText(account!!.nickName)
            .setListener(object : InputDialogListener {
                override fun confirm(content: String) {
                    tempNickname = content
                    vm?.updateWalletName(account!!, tempNickname)
                }
            }).show()
    }

    private val showQr: () -> Unit = {
        account?.let {
            QRDialog(this).show(it.nickName, it.address)
        }
    }

    private val fingerprint: (Int) -> Unit = {
        this.type = it
        FingerprintUtils.getFingerprint(
            this,
            null,
            true,
            { onFingerprintAuthenticateSucceed() }
        ).authenticate()
    }

    private fun onFingerprintAuthenticateSucceed() {
        account?.apply {
            if (type == TYPE_BACKUP) {
                BackupActivity.start(
                    this@WalletDetailActivity,
                    BackupActivity.FROM_WALLET_MANAGER,
                    this,
                    KeyUtils.entropy2Mnemonic(getEntropyAsHex()) as ArrayList<String>
                )
            } else if (type == TYPE_DELETE) {
                showLoading()
                vm?.deleteAccount(this)
                finish()
            }
        }
    }

    companion object {
        private const val TYPE_BACKUP = 1
        private const val TYPE_DELETE = 2

        fun start(context: Context, account: Account) {
            val intent = Intent(context, WalletDetailActivity::class.java)
            intent.putExtra(ExtraKey.SERIALIZABLE, account)
            context.startActivity(intent)
        }
    }
}