package com.highstreet.wallet.ui.activity

import android.app.Activity
import android.content.Intent
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.hao.library.view.dialog.ConfirmDialog
import com.hao.library.view.dialog.ConfirmDialogListener
import com.hao.library.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.databinding.ActivityCreatePasswordBinding
import com.highstreet.wallet.db.Password
import com.highstreet.wallet.extensions.focusListener
import com.highstreet.wallet.extensions.isPassword
import com.highstreet.wallet.extensions.string
import com.highstreet.wallet.fingerprint.FingerprintUtils
import com.highstreet.wallet.ui.vm.CreatePasswordVM

/**
 * @author Yang Shihao
 * @Date 2020/10/22
 */
@AndroidEntryPoint
class CreatePasswordActivity : BaseActivity<ActivityCreatePasswordBinding, CreatePasswordVM>() {

    private var password: Password? = null

    override fun initView() {
        setTitle(R.string.cpa_createPassword)
        viewBinding {
            etPassword.focusListener(passwordLine.line)
            etConfirmPassword.focusListener(confirmPasswordLine.line)
            RxView.textChanges(etPassword, etConfirmPassword) {
                btnCreate.isEnabled = etPassword.string().isNotEmpty()
                        && etConfirmPassword.string().isNotEmpty()
            }

            RxView.click(btnCreate, this@CreatePasswordActivity::createPassword)
        }
    }

    override fun initData() {
        viewModel {
            createPasswordLD.observe(this@CreatePasswordActivity) {
                hideLoading()
                if (null == it) {
                    toast(R.string.failed)
                } else {
                    password = it
                    toast(R.string.succeed)
                    setFingerprint()
                }
            }

            fingerprintLD.observe(this@CreatePasswordActivity) {
                hideLoading()
                if (true == it) {
                    back()
                } else {
                    toast(R.string.cpa_saveFingerprintFailed)
                }
            }
        }
    }

    private fun createPassword() {
        val password = vb!!.etPassword.string()
        val confirmPassword = vb!!.etConfirmPassword.string()

        if (!password.isPassword()) {
            toast(R.string.cpa_passwordFormatError)
            return
        }

        if (password != confirmPassword) {
            toast(R.string.cpa_passwordNotEqual)
            return
        }
        showLoading()
        vm!!.createPassword(password)
    }

    private fun setFingerprint() {
        if (FingerprintUtils.isAvailable(this)) {
            ConfirmDialog.Builder(this)
                .setMessage(getString(R.string.cpa_addFingerprintVerification))
                .setListener(object : ConfirmDialogListener {
                    override fun confirm() {
                        authenticateFingerprint()
                    }

                    override fun cancel() {
                        back()
                    }

                }).build().show()
        } else {
            back()
        }
    }

    private fun authenticateFingerprint() {
        FingerprintUtils.getFingerprint(
            this,
            useFingerprint = true,
            showUserPassword = false, {
                vm!!.setFingerprint(password!!)
            }, {
                back()
            }
        ).authenticate()
    }

    private fun back() {
        setResult(RESULT_OK)
        finish()
    }

    companion object {
        const val REQUEST_CODE_CREATE_PASSWORD = 101

        fun start(context: Activity) {
            context.startActivityForResult(
                Intent(context, CreatePasswordActivity::class.java),
                REQUEST_CODE_CREATE_PASSWORD
            )
        }
    }
}