package com.highstreet.wallet.ui.activity

import androidx.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.hao.library.AppManager
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.Colors
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.databinding.ActivityDelegationBinding
import com.highstreet.wallet.extensions.isAmount
import com.highstreet.wallet.extensions.string
import com.highstreet.wallet.fingerprint.FingerprintUtils
import com.highstreet.wallet.model.res.Validator
import com.highstreet.wallet.utils.StringUtils
import com.highstreet.wallet.ui.vm.DelegationVM
import com.highstreet.wallet.view.listener.RxView

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
@AndroidEntryPoint
class DelegationActivity : BaseActivity<ActivityDelegationBinding, DelegationVM>(),
    View.OnFocusChangeListener {

    private var amount = 0L

    private var validator: Validator? = null

    override fun initView() {
        setTitle(R.string.delegation)
        viewBinding {
            etAmount.onFocusChangeListener = this@DelegationActivity
            etRemarks.onFocusChangeListener = this@DelegationActivity

            RxView.textChanges(etAmount) {
                btnConfirm.isEnabled = etAmount.string().isNotEmpty()
            }

            RxView.click(btnConfirm) {
                delegate()
            }
        }
    }

    private fun delegate() {
        val s = vb!!.etAmount.string()
        if (TextUtils.isEmpty(s) || !s.isAmount()) {
            toast(R.string.amountFormatError)
            return
        }

        val l = s.toLong()
        if (l > amount) {
            toast(R.string.notEnough)
            return
        }

        FingerprintUtils.getFingerprint(
            this,
            useFingerprint = null,
            showUserPassword = true, {
                showLoading()
                vm!!.delegate(vb!!.etAddress.string(), s, vb!!.etRemarks.string())
            }
        ).authenticate()
    }

    private fun updateLineStyle(view: View, hasFocus: Boolean) {
        view.setBackgroundColor(if (hasFocus) Colors.editLineFocus else Colors.editLineBlur)
    }

    override fun initData() {
        validator = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as Validator?
        validator?.apply {
            vb!!.etAddress.setText(operator_address)
        }

        viewModel {
            amountLD.observe(this@DelegationActivity, Observer {
                it?.apply {
                    amount = getLongAmount()
                    vb!!.tvBalance.text =
                        "${getString(R.string.availableBalance)}${StringUtils.pdip2DIP(getAmount())}"
                }
            })
            resultLD.observe(this@DelegationActivity, Observer {
                hideLoading()
                toast(it.second)
                if (it.first) {
                    AppManager.instance().finishActivity(DelegationDetailActivity::class.java)
                    finish()
                }
            })
            getAccountInfo(AccountManager.instance().address)
        }
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        viewBinding {
            when (v) {
                etAmount -> updateLineStyle(amountLine.line, hasFocus)
                etRemarks -> updateLineStyle(remarksLine.line, hasFocus)
            }
        }
    }

    companion object {
        fun start(context: Context, validator: Validator) {
            val intent = Intent(context, DelegationActivity::class.java)
            intent.putExtra(ExtraKey.SERIALIZABLE, validator)
            context.startActivity(intent)
        }
    }
}