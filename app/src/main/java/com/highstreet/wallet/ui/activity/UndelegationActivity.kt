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
import com.highstreet.wallet.model.res.DelegationInfo
import com.highstreet.wallet.utils.AmountUtils
import com.highstreet.wallet.utils.StringUtils
import com.highstreet.wallet.ui.vm.UndelegationVM
import kotlinx.android.synthetic.main.g_activity_undelegation.*

/**
 * @author Yang Shihao
 * @Date 2020/10/27
 */
class UndelegationActivity : BaseActivity(), View.OnFocusChangeListener {

    private var amount = "0"

    private var delegationInfo: DelegationInfo? = null

    private val viewModel by lazy {
        ViewModelProvider(this).get(UndelegationVM::class.java)
    }

    override fun getLayoutId() = R.layout.g_activity_undelegation

    override fun initView() {
        setTitle(R.string.undelegate)
        etAmount.onFocusChangeListener = this
        etRemarks.onFocusChangeListener = this

        RxView.textChanges(etAmount) {
            btnConfirm.isEnabled = etAmount.string().isNotEmpty()
        }

        RxView.click(tvAll) {
            etAmount.setText(amount)
            etAmount.setSelection(etAmount.string().length)
        }

        RxView.click(btnConfirm) {
            unDelegate()
        }
    }

    override fun initData() {
        delegationInfo = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as DelegationInfo?
        delegationInfo?.apply {
            etAddress.setText(validator_address)
            amount = shares ?: "0"
            tvMaxAmount.text =
                "${getString(R.string.undelegateMaxAmount)}${StringUtils.pdip2DIP(amount)}"
        }
        viewModel.undelegateLD.observe(this, Observer {
            hideLoading()
            if (it.first) {
                toast(R.string.succeed)
                AppManager.instance().finishActivity(DelegationDetailActivity::class.java)
                finish()
            } else {
                toast(R.string.failed)
            }
        })
    }

    private fun unDelegate() {
        if (delegationInfo == null) {
            return
        }
        val s = etAmount.string()
        if (TextUtils.isEmpty(s) || !s.isAmount()) {
            toast(R.string.amountFormatError)
            return
        }

        if (!AmountUtils.isEnough(amount, s)) {
            toast(R.string.overMaxAmount)
            return
        }

        getFingerprint(
            FingerprintUtils.isAvailable(this) && AccountManager.instance().fingerprint,
            true
        )?.authenticate()
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        when (v) {
            etAmount -> updateLineStyle(amountLine, hasFocus)
            etRemarks -> updateLineStyle(remarksLine, hasFocus)
        }
    }

    private fun updateLineStyle(view: View, hasFocus: Boolean) {
        view.setBackgroundColor(if (hasFocus) Colors.editLineFocus else Colors.editLineBlur)
    }

    override fun onFingerprintAuthenticateSucceed() {
        showLoading()
        viewModel.undelegate(etAmount.string(), delegationInfo!!)
    }

    override fun usePassword(password: String): Boolean {
        if (!AccountManager.instance().password!!.verify(password)) {
            return false
        }
        onFingerprintAuthenticateSucceed()
        return true
    }

    companion object {
        fun start(context: Context, delegationInfo: DelegationInfo) {
            val intent = Intent(context, UndelegationActivity::class.java)
            intent.putExtra(ExtraKey.SERIALIZABLE, delegationInfo)
            context.startActivity(intent)
        }
    }
}