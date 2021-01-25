package com.highstreet.wallet.ui.activity

import androidx.lifecycle.Observer
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.highstreet.lib.adapter.BaseNormalAdapter
import com.highstreet.lib.fingerprint.FingerprintUtils
import com.highstreet.lib.ui.BaseSimpleListActivity
import com.highstreet.lib.view.dialog.ConfirmDialog
import com.highstreet.lib.view.dialog.ConfirmDialogListener
import com.highstreet.lib.view.dialog.InputDialog
import com.highstreet.lib.view.dialog.InputDialogListener
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.ui.adapter.WalletManageAdapter
import com.highstreet.wallet.db.Account
import com.highstreet.wallet.db.Db
import com.highstreet.wallet.extensions.copy
import com.highstreet.wallet.ui.vm.WalletManageVM
import com.highstreet.wallet.crypto.KeyUtils
import kotlinx.android.synthetic.main.g_activity_wallet_manage.*

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */

class WalletManageActivity : BaseSimpleListActivity<Account>() {

    /**
     * 当前操作的Account
     */
    private var useAccount: Account? = null

    private var type = 0

    private lateinit var walletManageAdapter: WalletManageAdapter

    private val viewModel by lazy {
        ViewModelProvider(this).get(WalletManageVM::class.java)
    }

    override fun showToolbar() = false

    override fun getLayoutId() = R.layout.g_activity_wallet_manage

    override fun createAdapter(): BaseNormalAdapter<Account> {
        walletManageAdapter = WalletManageAdapter(this)
        return walletManageAdapter
    }

    override fun initView() {
        super.initView()
        title = "钱包管理"
        RxView.click(ivAdd) {
            InitWalletActivity.start(this, true)
        }
    }

    override fun initData() {
        super.initData()
        Db.instance().accountDao().queryAllByChainAsLiveData(AccountManager.instance().chain)
            .observe(this, Observer {
                if (it.isEmpty()) {
                    InitWalletActivity.start(this)
                } else {
                    walletManageAdapter.resetData(it)
                }

            })
        viewModel.updateNameLD.observe(this, Observer {
            hideLoading()
            if (true == it) {
                toast("修改成功")
            } else {

                toast("修改失败")
            }
        })
        viewModel.deleteLD.observe(this, Observer {
            hideLoading()
            if (true == it) {
                toast("删除成功")
            } else {

                toast("删除失败")
            }
        })
    }

    override fun itemClicked(view: View, item: Account, position: Int) {
        when (view.id) {
            R.id.tvWalletAddress, R.id.ivCopy -> item.address?.copy(this)
            R.id.tvBackup -> {
                type = TYPE_BACKUP
                useAccount = item
                getFingerprint(FingerprintUtils.isAvailable(this), true)?.authenticate()
            }
            R.id.ivEdit -> {
                InputDialog(this)
                    .setTitle("修改钱包名称")
                    .setHint("请输入新的钱包名称")
                    .setText(item.nickName)
                    .setListener(object : InputDialogListener {
                        override fun confirm(content: String) {
                            viewModel.updateWalletName(item, content)
                        }
                    }).show()
            }
            R.id.ivDelete -> {
                useAccount = item
                ConfirmDialog(this).setMsg("确认删除钱包${item.nickName}?")
                    .setListener(object : ConfirmDialogListener {
                        override fun confirm() {
                            type = TYPE_DELETE
                            useAccount = item
                            getFingerprint(
                                FingerprintUtils.isAvailable(this@WalletManageActivity),
                                true
                            )?.authenticate()
                        }

                        override fun cancel() {

                        }

                    }).show()
            }
            else -> {
                viewModel.changeLastAccount(item)
            }
        }
    }

    override fun onFingerprintAuthenticateSucceed() {
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
                viewModel.deleteAccount(this)
            }
        }
    }

    override fun usePassword(password: String): Boolean {
        if (!AccountManager.instance().password!!.verify(password)) {
            return false
        }
        onFingerprintAuthenticateSucceed()
        return true
    }

    companion object {
        private const val TYPE_BACKUP = 1
        private const val TYPE_DELETE = 2
    }
}