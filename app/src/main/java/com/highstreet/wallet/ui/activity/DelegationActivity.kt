package com.highstreet.wallet.ui.activity

import androidx.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.highstreet.lib.common.AppManager
import com.highstreet.lib.extensions.string
import com.highstreet.lib.fingerprint.FingerprintUtils
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.Colors
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.extensions.isAmount
import com.highstreet.wallet.model.res.Validator
import com.highstreet.wallet.utils.StringUtils
import com.highstreet.wallet.ui.vm.DelegationVM
import kotlinx.android.synthetic.main.g_activity_delegation.*

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
class DelegationActivity : BaseActivity(), View.OnFocusChangeListener {

    private var amount = 0L

    private var validator: Validator? = null

    private val viewModel by lazy {
        ViewModelProvider(this).get(DelegationVM::class.java)
    }

    override fun getLayoutId() = R.layout.g_activity_delegation

    override fun initView() {
        setTitle(R.string.delegation)
        etAmount.onFocusChangeListener = this
        etRemarks.onFocusChangeListener = this

        RxView.textChanges(etAmount) {
            btnConfirm.isEnabled = etAmount.string().isNotEmpty()
        }

        RxView.click(btnConfirm) {
            delegate()
        }
    }

    private fun delegate() {
        val s = etAmount.string()
        if (TextUtils.isEmpty(s) || !s.isAmount()) {
            toast(R.string.amountFormatError)
            return
        }

        val l = s.toLong()
        if (l > amount) {
            toast(R.string.notEnough)
            return
        }

        getFingerprint(
            FingerprintUtils.isAvailable(this) && AccountManager.instance().fingerprint,
            true
        )?.authenticate()
    }

    private fun updateLineStyle(view: View, hasFocus: Boolean) {
        view.setBackgroundColor(if (hasFocus) Colors.editLineFocus else Colors.editLineBlur)
    }

    override fun initData() {
        validator = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as Validator?
        validator?.apply {
            etAddress.setText(operator_address)
        }

        viewModel.amountLD.observe(this, Observer {
            it?.apply {
                amount = getLongAmount()
                tvBalance.text =
                    "${getString(R.string.availableBalance)}${StringUtils.pdip2DIP(getAmount())}"
            }
        })
        viewModel.resultLD.observe(this, Observer {
            hideLoading()
            toast(it.second)
            if (it.first) {
                AppManager.instance().finishActivity(DelegationDetailActivity::class.java)
                finish()
            }
        })
        viewModel.getAccountInfo(AccountManager.instance().address)
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        when (v) {
            etAmount -> updateLineStyle(amountLine, hasFocus)
            etRemarks -> updateLineStyle(remarksLine, hasFocus)
        }
    }

    override fun onFingerprintAuthenticateSucceed() {
        showLoading()
        viewModel.delegate(etAddress.string(), etAmount.string(), etRemarks.string())
    }

    override fun usePassword(password: String): Boolean {
        if (!AccountManager.instance().password!!.verify(password)) {
            return false
        }
        onFingerprintAuthenticateSucceed()
        return true
    }

    companion object {
        fun start(context: Context, validator: Validator) {
            val intent = Intent(context, DelegationActivity::class.java)
            intent.putExtra(ExtraKey.SERIALIZABLE, validator)
            context.startActivity(intent)
        }
    }
}