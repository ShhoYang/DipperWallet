package com.highstreet.wallet.ui.activity

import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.hao.library.utils.CoroutineUtils
import com.hao.library.view.dialog.ConfirmDialog
import com.hao.library.view.dialog.ConfirmDialogListener
import com.hao.library.view.listener.RxView
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.Constant
import com.highstreet.wallet.databinding.ActivityLockBinding
import com.highstreet.wallet.db.Db
import com.highstreet.wallet.fingerprint.FingerprintUtils

@AndroidEntryPoint(injectViewModel = false)
class LockActivity : BaseActivity<ActivityLockBinding, PlaceholderViewModel>() {

    private var setFingerprint = false

    override fun initView() {
        setTitle(R.string.la_fingerprint)
        viewBinding {
            RxView.click(ivSwitch, this@LockActivity::openOrClose)
        }
    }

    private fun openOrClose() {
        when {
            setFingerprint -> {
                ConfirmDialog.Builder(this@LockActivity)
                    .setMessage(getString(R.string.la_cancelFingerprintVerification))
                    .setListener(object : ConfirmDialogListener {
                        override fun confirm() {
                            authenticateFingerprint()
                        }

                        override fun cancel() {
                        }

                    }).build().show()
            }
            FingerprintUtils.hasEnrolledFingerprints(this@LockActivity) -> authenticateFingerprint()
            else -> toast(R.string.la_noFingerprint)
        }
    }

    private fun authenticateFingerprint() {
        FingerprintUtils.getFingerprint(
            this,
            useFingerprint = true,
            showUserPassword = false, {
                setFingerprint = !setFingerprint
                AccountManager.instance().password?.let {
                    CoroutineUtils.io {
                        it.fingerprint = setFingerprint
                        AccountManager.instance().updatePassword(it)
                    }
                }
            }, {
            }
        ).authenticate()
    }

    override fun initData() {
        Db.instance().passwordDao().queryByIdAsLiveData(Constant.PASSWORD_DEFAULT_ID)
            .observe(this) {
                setFingerprint = it?.fingerprint ?: false
                vb?.ivSwitch?.setImageResource(if (setFingerprint) R.drawable.ic_switch_open else R.drawable.ic_switch_close)
            }
    }
}
