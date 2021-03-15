package com.highstreet.wallet.ui.activity

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.hao.library.utils.DateUtils
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
import com.highstreet.wallet.view.listener.RxView

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */
@AndroidEntryPoint
class WalletDetailActivity :
    BaseActivity<ActivityWalletDetailBinding, WalletDetailVM>(), View.OnClickListener {

    private var tempNickname = ""
    private var account: Account? = null

    private var type = 0

    override fun initView() {
        setTitle(R.string.walletManage)
        viewBinding {
            RxView.click(ivEditName, this@WalletDetailActivity)
            RxView.click(ivWalletAddress, this@WalletDetailActivity)
            RxView.click(btnBackup, this@WalletDetailActivity)
            RxView.click(btnDelete, this@WalletDetailActivity)
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
            updateNameLD.observe(this@WalletDetailActivity, Observer {
                hideLoading()
                if (it) {
                    toast(R.string.updateSucceed)
                    account?.nickName = tempNickname
                    vb?.tvWalletName?.text = tempNickname
                } else {
                    toast(R.string.updateFailed)
                }
            })
            deleteLD.observe(this@WalletDetailActivity, Observer {
                hideLoading()
                if (it) {
                    toast(R.string.deleteSucceed)
                    if (AccountManager.instance().accounts.isEmpty()) {
                        InitWalletActivity.start(this@WalletDetailActivity)
                    } else {
                        finish()
                    }
                } else {
                    toast(R.string.deleteFailed)
                }
            })
        }
    }

    override fun onClick(v: View?) {
        viewBinding {
            when (v) {
                ivEditName -> editName()
                ivWalletAddress -> showQr()
                btnBackup -> fingerprint(TYPE_BACKUP)
                btnDelete -> fingerprint(TYPE_DELETE)
            }
        }
    }

    private fun editName() {
        InputDialog(this)
            .setTitle(getString(R.string.updateWalletName))
            .setHint(getString(R.string.inputNewWalletName))
            .setText(account!!.nickName)
            .setListener(object : InputDialogListener {
                override fun confirm(content: String) {
                    tempNickname = content
                    vm?.updateWalletName(account!!, tempNickname)
                }
            }).show()
    }

    private fun showQr() {
        account?.let {
            QRDialog(this).show(it.nickName, it.address)
        }
    }

    private fun fingerprint(type: Int) {
        this.type = type
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