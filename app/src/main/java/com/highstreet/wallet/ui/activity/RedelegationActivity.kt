package com.highstreet.wallet.ui.activity

import android.app.Activity
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
import com.highstreet.wallet.model.res.Validator
import com.highstreet.wallet.utils.AmountUtils
import com.highstreet.wallet.utils.StringUtils
import com.highstreet.wallet.ui.vm.RedelegationVM
import kotlinx.android.synthetic.main.g_activity_redelegation.*

/**
 * @author Yang Shihao
 * @Date 2020/10/27
 *
 * 转委托
 */
class RedelegationActivity : BaseActivity(), View.OnClickListener, View.OnFocusChangeListener {

    private var amount = "0"

    private var delegationInfo: DelegationInfo? = null
    private var validator: Validator? = null

    private val viewModel by lazy {
        ViewModelProvider(this).get(RedelegationVM::class.java)
    }

    override fun getLayoutId() = R.layout.g_activity_redelegation

    override fun initView() {
        setTitle(R.string.redelegate)
        etAmount.onFocusChangeListener = this
        etRemarks.onFocusChangeListener = this

        RxView.textChanges(etAmount) {
            btnConfirm.isEnabled = etAmount.string().isNotEmpty()
        }

        RxView.click(llAddress, this)
        RxView.click(tvAll, this)
        RxView.click(btnConfirm, this)
    }

    override fun initData() {
        delegationInfo = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as DelegationInfo?
        delegationInfo?.apply {
            amount = shares ?: "0"
            tvMaxAmount.text =
                "${getString(R.string.redelegateMaxAmount)} ${StringUtils.pdip2DIP(amount)}"
        }
        viewModel.redelegateLD.observe(this, Observer {
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

    private fun redelegate() {
        if (delegationInfo == null) {
            return
        }

        if (etAddress.string().isEmpty()) {
            toast(R.string.pleaseSelectvalidator)
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

    override fun onClick(v: View?) {
        when (v) {
            llAddress -> ValidatorListActivity.start(this, true)
            tvAll -> {
                etAmount.setText(amount)
                etAmount.setSelection(etAmount.string().length)
            }
            btnConfirm -> redelegate()
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ValidatorListActivity.REQUEST_CODE_VALIDATOR_CHOOSE
            && resultCode == Activity.RESULT_OK
            && data != null
        ) {
            val validator = data.getSerializableExtra(ExtraKey.SERIALIZABLE) as Validator?
            if (delegationInfo != null && delegationInfo!!.validator_address == validator?.operator_address) {
                toast(R.string.sameValidator)
            } else {
                this.validator = validator
                this.validator?.apply {
                    etAddress.setText(operator_address)
                }
            }
        }
    }

    override fun onFingerprintAuthenticateSucceed() {
        showLoading()
        viewModel.redelegate(etAmount.string(), delegationInfo!!, etAddress.string())
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
            val intent = Intent(context, RedelegationActivity::class.java)
            intent.putExtra(ExtraKey.SERIALIZABLE, delegationInfo)
            context.startActivity(intent)
        }
    }
}