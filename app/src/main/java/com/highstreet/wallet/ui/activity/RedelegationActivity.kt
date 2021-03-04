package com.highstreet.wallet.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import com.hao.library.AppManager
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.Colors
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.databinding.ActivityRedelegationBinding
import com.highstreet.wallet.extensions.isAmount
import com.highstreet.wallet.extensions.string
import com.highstreet.wallet.fingerprint.FingerprintUtils
import com.highstreet.wallet.model.res.DelegationInfo
import com.highstreet.wallet.model.res.Validator
import com.highstreet.wallet.ui.vm.RedelegationVM
import com.highstreet.wallet.utils.AmountUtils
import com.highstreet.wallet.utils.StringUtils
import com.highstreet.wallet.view.listener.RxView

/**
 * @author Yang Shihao
 * @Date 2020/10/27
 *
 * 转委托
 */
@AndroidEntryPoint
class RedelegationActivity : BaseActivity<ActivityRedelegationBinding, RedelegationVM>(),
    View.OnClickListener, View.OnFocusChangeListener {

    private var amount = "0"

    private var delegationInfo: DelegationInfo? = null
    private var validator: Validator? = null

    override fun initView() {
        setTitle(R.string.redelegate)
        viewBinding {
            etAmount.onFocusChangeListener = this@RedelegationActivity
            etRemarks.onFocusChangeListener = this@RedelegationActivity

            RxView.textChanges(etAmount) {
                btnConfirm.isEnabled = etAmount.string().isNotEmpty()
            }

            RxView.click(llAddress, this@RedelegationActivity)
            RxView.click(tvAll, this@RedelegationActivity)
            RxView.click(btnConfirm, this@RedelegationActivity)
        }
    }

    override fun initData() {
        delegationInfo = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as DelegationInfo?
        delegationInfo?.apply {
            amount = shares ?: "0"
            vb?.tvMaxAmount?.text =
                "${getString(R.string.redelegateMaxAmount)} ${StringUtils.pdip2DIP(amount)}"
        }
        vm?.redelegateLD?.observe(this, Observer {
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

        val address = vb?.etAddress?.string() ?: ""

        if (address.isEmpty()) {
            toast(R.string.pleaseSelectvalidator)
            return
        }

        val s = vb?.etAmount?.string() ?: ""
        if (TextUtils.isEmpty(s) || !s.isAmount()) {
            toast(R.string.amountFormatError)
            return
        }
        if (!AmountUtils.isEnough(amount, s)) {
            toast(R.string.overMaxAmount)
            return
        }

        FingerprintUtils.getFingerprint(
            this,
            null,
            true,
            {
                showLoading()
                vm?.redelegate(s, delegationInfo!!, address)
            },
        ).authenticate()
    }

    override fun onClick(v: View?) {
        viewBinding {
            when (v) {
                llAddress -> ValidatorChooseActivity.start(this@RedelegationActivity)
                tvAll -> {
                    etAmount.setText(amount)
                    etAmount.setSelection(etAmount.string().length)
                }
                btnConfirm -> redelegate()
            }
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

    private fun updateLineStyle(view: View, hasFocus: Boolean) {
        view.setBackgroundColor(if (hasFocus) Colors.editLineFocus else Colors.editLineBlur)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ValidatorChooseActivity.REQUEST_CODE_VALIDATOR_CHOOSE
            && resultCode == Activity.RESULT_OK
            && data != null
        ) {
            val validator = data.getSerializableExtra(ExtraKey.SERIALIZABLE) as Validator?
            if (delegationInfo != null && delegationInfo!!.validator_address == validator?.operator_address) {
                toast(R.string.sameValidator)
            } else {
                this.validator = validator
                this.validator?.apply {
                    vb?.etAddress?.setText(operator_address)
                }
            }
        }
    }

    companion object {
        fun start(context: Context, delegationInfo: DelegationInfo) {
            val intent = Intent(context, RedelegationActivity::class.java)
            intent.putExtra(ExtraKey.SERIALIZABLE, delegationInfo)
            context.startActivity(intent)
        }
    }
}