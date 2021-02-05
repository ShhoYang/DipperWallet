package com.highstreet.wallet.ui.activity

import android.app.Activity
import androidx.lifecycle.Observer
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.highstreet.lib.extensions.string
import com.highstreet.lib.fingerprint.FingerprintUtils
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.lib.view.dialog.ConfirmDialog
import com.highstreet.lib.view.dialog.ConfirmDialogListener
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.Colors
import com.highstreet.wallet.db.Password
import com.highstreet.wallet.extensions.isPassword
import com.highstreet.wallet.ui.vm.CreatePasswordVM
import kotlinx.android.synthetic.main.g_activity_create_password.*

/**
 * @author Yang Shihao
 * @Date 2020/10/22
 */
class CreatePasswordActivity : BaseActivity(), View.OnFocusChangeListener {

    private var password: Password? = null

    private val viewModel by lazy {
        ViewModelProvider(this).get(CreatePasswordVM::class.java)
    }

    override fun getLayoutId() = R.layout.g_activity_create_password

    override fun initView() {
        setTitle(R.string.createPassword)

        etPassword.onFocusChangeListener = this
        etConfirmPassword.onFocusChangeListener = this


        RxView.textChanges(etPassword, etConfirmPassword) {
            btnCreate.isEnabled = etPassword.string().isNotEmpty()
                    && etConfirmPassword.string().isNotEmpty()
        }

        RxView.click(btnCreate) {
            createPassword()
        }
    }

    private fun updateLineStyle(view: View, hasFocus: Boolean) {
        view.setBackgroundColor(if (hasFocus) Colors.editLineFocus else Colors.editLineBlur)
    }

    override fun initData() {
        viewModel.createPasswordLD.observe(this, Observer {
            hideLoading()
            if (null == it) {
                toast(R.string.failed)
            } else {
                password = it
                toast(R.string.succeed)
                setFingerprint()
            }
        })

        viewModel.fingerprintLD.observe(this, Observer {
            hideLoading()
            if (true == it) {
                back()
            } else {
                toast(R.string.saveFingerprintFailed)
            }
        })
    }

    private fun createPassword() {
        val password = etPassword.string()
        val confirmPassword = etConfirmPassword.string()

        if (!password.isPassword()) {
            toast(R.string.passwordFormatError)
            return
        }

        if (password != confirmPassword) {
            toast(R.string.passwordNotEqual)
            return
        }

        showLoading()
        viewModel.createPassword(password)
    }

    private fun setFingerprint() {
        if (FingerprintUtils.isAvailable(this)) {
            ConfirmDialog(this).setMsg(getString(R.string.addFingerprintVerification))
                .setListener(object : ConfirmDialogListener {
                    override fun confirm() {
                        getFingerprint(
                            useFingerprint = true,
                            showUserPassword = false
                        )?.authenticate()
                    }

                    override fun cancel() {
                        back()
                    }
                }).show()

        } else {
            back()
        }
    }

    override fun onFingerprintAuthenticateSucceed() {
        viewModel.setFingerprint(password!!)
    }

    override fun onFingerprintCancel() {
        back()
    }

    private fun back() {
        setResult(RESULT_OK)
        finish()
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        when (v) {
            etPassword -> updateLineStyle(passwordLine, hasFocus)
            etConfirmPassword -> updateLineStyle(confirmPasswordLine, hasFocus)
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