package com.highstreet.wallet.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.hao.library.AppManager
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.hao.library.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.databinding.ActivityRedelegateBinding
import com.highstreet.wallet.extensions.focusListener
import com.highstreet.wallet.extensions.isAmount
import com.highstreet.wallet.extensions.string
import com.highstreet.wallet.fingerprint.FingerprintUtils
import com.highstreet.wallet.model.res.DelegationInfo
import com.highstreet.wallet.model.res.Validator
import com.highstreet.wallet.ui.vm.RedelegateVM
import com.highstreet.wallet.utils.AmountUtils

/**
 * @author Yang Shihao
 * @Date 2020/10/27
 *
 * 转委托
 */
@AndroidEntryPoint
class RedelegateActivity : BaseActivity<ActivityRedelegateBinding, RedelegateVM>() {

    private var amount = "0"

    private var delegationInfo: DelegationInfo? = null
    private var validator: Validator? = null

    override fun initView() {
        setTitle(R.string.ra_redelegate)
        viewBinding {

            etAmount.focusListener(amountLine.line)
            etMemo.focusListener(memoLine.line)

            RxView.textChanges(etAmount) {
                btnConfirm.isEnabled = etAmount.string().isNotEmpty()
            }

            RxView.click(llAddress, this@RedelegateActivity, ValidatorChooseActivity.start)
            RxView.click(tvAll) {
                etAmount.setText(amount)
                etAmount.setSelection(etAmount.string().length)
            }
            RxView.click(btnConfirm) {
                redelegate()
            }
        }
    }

    override fun initData() {
        delegationInfo = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as DelegationInfo?
        delegationInfo?.apply {
            amount = shares ?: "0"
            vb?.tvMaxAmount?.text =
                "${getString(R.string.ra_redelegateMaxAmount)} ${AmountUtils.pdip2DIP(amount)}"
        }
        vm?.redelegateLD?.observe(this) {
            hideLoading()
            toast(it.second)
            if (it.first) {

                AppManager.instance().finishActivity(DelegationDetailActivity::class.java)
                finish()
            }
        }
    }

    private fun redelegate() {
        if (delegationInfo == null) {
            return
        }

        val address = vb?.etAddress?.string() ?: ""

        if (address.isEmpty()) {
            toast(R.string.ra_pleaseSelectvalidator)
            return
        }

        val s = vb?.etAmount?.string() ?: ""
        if (TextUtils.isEmpty(s) || !s.isAmount()) {
            toast(R.string.amountFormatError)
            return
        }
        if (!AmountUtils.isEnough(amount, s)) {
            toast(R.string.ra_overMaxAmount)
            return
        }

        FingerprintUtils.getFingerprint(
            this,
            null,
            true,
            {
                showLoading()
                vm?.redelegate(s, delegationInfo!!, address, vb?.etAmount?.string() ?: "")
            },
        ).authenticate()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ValidatorChooseActivity.REQUEST_CODE_VALIDATOR_CHOOSE
            && resultCode == Activity.RESULT_OK
            && data != null
        ) {
            val validator = data.getSerializableExtra(ExtraKey.SERIALIZABLE) as Validator?
            if (delegationInfo != null && delegationInfo!!.validator_address == validator?.operator_address) {
                toast(R.string.ra_sameValidator)
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
            val intent = Intent(context, RedelegateActivity::class.java)
            intent.putExtra(ExtraKey.SERIALIZABLE, delegationInfo)
            context.startActivity(intent)
        }
    }
}