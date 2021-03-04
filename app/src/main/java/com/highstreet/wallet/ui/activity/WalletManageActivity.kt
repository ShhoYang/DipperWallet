package com.highstreet.wallet.ui.activity

import androidx.lifecycle.Observer
import android.view.View
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseNormalListActivity
import com.hao.library.view.dialog.ConfirmDialog
import com.hao.library.view.dialog.ConfirmDialogListener
import com.highstreet.wallet.R
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.ui.adapter.WalletManageAdapter
import com.highstreet.wallet.db.Account
import com.highstreet.wallet.db.Db
import com.highstreet.wallet.extensions.copy
import com.highstreet.wallet.ui.vm.WalletManageVM
import com.highstreet.wallet.crypto.KeyUtils
import com.highstreet.wallet.databinding.ActivityWalletManageBinding
import com.highstreet.wallet.fingerprint.FingerprintUtils
import com.highstreet.wallet.view.InputDialog
import com.highstreet.wallet.view.InputDialogListener
import com.highstreet.wallet.view.listener.RxView

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */
@AndroidEntryPoint
class WalletManageActivity :
    BaseNormalListActivity<ActivityWalletManageBinding, Account, WalletManageVM, WalletManageAdapter>() {

    /**
     * 当前操作的Account
     */
    private var useAccount: Account? = null

    private var type = 0

    override fun initView() {
        super.initView()
        setTitle(R.string.walletManager)
        RxView.click(vb!!.ivAdd) {
            InitWalletActivity.start(this, true)
        }
    }

    override fun initData() {
        Db.instance().accountDao().queryAllByChainAsLiveData(AccountManager.instance().chain)
            .observe(this, Observer {
                if (it.isEmpty()) {
                    InitWalletActivity.start(this)
                } else {
                    adapter.resetData(it)
                }

            })
        viewModel {
            updateNameLD.observe(this@WalletManageActivity, Observer {
                hideLoading()
                if (it) {
                    toast(R.string.updateSucceed)
                } else {
                    toast(R.string.updateFailed)
                }
            })
            deleteLD.observe(this@WalletManageActivity, Observer {
                hideLoading()
                if (it) {
                    toast(R.string.deleteSucceed)
                } else {
                    toast(R.string.deleteFailed)
                }
            })
        }
    }

    override fun itemClicked(view: View, item: Account, position: Int) {
        when (view.id) {
            R.id.tvWalletAddress, R.id.ivCopy -> item.address?.copy(this)
            R.id.tvBackup -> fingerprint(TYPE_BACKUP, item)
            R.id.ivEdit -> {
                InputDialog(this)
                    .setTitle(getString(R.string.updateWalletName))
                    .setHint(getString(R.string.inputNewWalletName))
                    .setText(item.nickName)
                    .setListener(object : InputDialogListener {
                        override fun confirm(content: String) {
                            vm?.updateWalletName(item, content)
                        }
                    }).show()
            }
            R.id.ivDelete -> {
                ConfirmDialog.Builder(this)
                    .setMessage("${getString(R.string.confirmDeleteWallet)}${item.nickName}?")
                    .setListener(object : ConfirmDialogListener {
                        override fun confirm() {
                            fingerprint(TYPE_DELETE, item)
                        }

                        override fun cancel() {

                        }

                    }).build().show()

            }
            else -> {
                vm?.changeLastAccount(item)
            }
        }
    }

    private fun fingerprint(type: Int, account: Account) {
        this.type = type
        useAccount = account
        FingerprintUtils.getFingerprint(
            this,
            null,
            true,
            { onFingerprintAuthenticateSucceed() },
        ).authenticate()
    }

    private fun onFingerprintAuthenticateSucceed() {
        useAccount?.apply {
            if (type == TYPE_BACKUP) {
                BackupActivity.start(
                    this@WalletManageActivity,
                    BackupActivity.FROM_WALLET_MANAGER,
                    this,
                    KeyUtils.entropy2Mnemonic(getEntropyAsHex()) as ArrayList<String>
                )
            } else if (type == TYPE_DELETE) {
                showLoading()
                vm?.deleteAccount(this)
            }
        }
    }

    companion object {
        private const val TYPE_BACKUP = 1
        private const val TYPE_DELETE = 2
    }
}