package com.highstreet.wallet.ui.activity

import android.app.Activity
import androidx.lifecycle.Observer
import android.content.Intent
import android.view.View
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.hao.library.view.dialog.ConfirmDialog
import com.hao.library.view.dialog.ConfirmDialogListener
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.Colors
import com.highstreet.wallet.databinding.ActivityCreatePasswordBinding
import com.highstreet.wallet.db.Password
import com.highstreet.wallet.extensions.isPassword
import com.highstreet.wallet.extensions.string
import com.highstreet.wallet.fingerprint.FingerprintUtils
import com.highstreet.wallet.ui.vm.CreatePasswordVM
import com.highstreet.wallet.view.listener.RxView

/**
 * @author Yang Shihao
 * @Date 2020/10/22
 */
@AndroidEntryPoint
class CreatePasswordActivity : BaseActivity<ActivityCreatePasswordBinding, CreatePasswordVM>(),
    View.OnFocusChangeListener {

    private var password: Password? = null

    override fun initView() {
        setTitle(R.string.createPassword)
        viewBinding {
            etPassword.onFocusChangeListener = this@CreatePasswordActivity
            etConfirmPassword.onFocusChangeListener = this@CreatePasswordActivity
            RxView.textChanges(etPassword, etConfirmPassword) {
                btnCreate.isEnabled = etPassword.string().isNotEmpty()
                        && etConfirmPassword.string().isNotEmpty()
            }

            RxView.click(btnCreate) {
                createPassword()
            }
        }
    }

    private fun updateLineStyle(view: View, hasFocus: Boolean) {
        view.setBackgroundColor(if (hasFocus) Colors.editLineFocus else Colors.editLineBlur)
    }

    override fun initData() {
        viewModel {
            createPasswordLD.observe(this@CreatePasswordActivity, Observer {
                hideLoading()
                if (null == it) {
                    toast(R.string.failed)
                } else {
                    password = it
                    toast(R.string.succeed)
                    setFingerprint()
                }
            })

            fingerprintLD.observe(this@CreatePasswordActivity, Observer {
                hideLoading()
                if (true == it) {
                    back()
                } else {
                    toast(R.string.saveFingerprintFailed)
                }
            })
        }
    }

    private fun createPassword() {
        val password = vb!!.etPassword.string()
        val confirmPassword = vb!!.etConfirmPassword.string()

        if (!password.isPassword()) {
            toast(R.string.passwordFormatError)
            return
        }

        if (password != confirmPassword) {
            toast(R.string.passwordNotEqual)
            return
        }
        showLoading()
        vm!!.createPassword(password)
    }

    private fun setFingerprint() {
        if (FingerprintUtils.isAvailable(this)) {
            ConfirmDialog.Builder(this)
                .setMessage(getString(R.string.addFingerprintVerification))
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

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        viewBinding {
            when (v) {
                etPassword -> updateLineStyle(passwordLine.line, hasFocus)
                etConfirmPassword -> updateLineStyle(confirmPasswordLine.line, hasFocus)
            }
        }
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