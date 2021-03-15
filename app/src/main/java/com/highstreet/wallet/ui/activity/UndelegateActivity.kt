package com.highstreet.wallet.ui.activity

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
import com.highstreet.wallet.databinding.ActivityUndelegateBinding
import com.highstreet.wallet.extensions.isAmount
import com.highstreet.wallet.extensions.string
import com.highstreet.wallet.fingerprint.FingerprintUtils
import com.highstreet.wallet.model.res.DelegationInfo
import com.highstreet.wallet.ui.vm.UndelegationVM
import com.highstreet.wallet.utils.AmountUtils
import com.highstreet.wallet.utils.StringUtils
import com.highstreet.wallet.view.listener.RxView

/**
 * @author Yang Shihao
 * @Date 2020/10/27
 */
@AndroidEntryPoint
class UndelegateActivity : BaseActivity<ActivityUndelegateBinding, UndelegationVM>(),
    View.OnFocusChangeListener {

    private var amount = "0"

    private var delegationInfo: DelegationInfo? = null

    override fun initView() {
        setTitle(R.string.undelegate)
        viewBinding {
            etAmount.onFocusChangeListener = this@UndelegateActivity
            etRemarks.onFocusChangeListener = this@UndelegateActivity

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
    }

    override fun initData() {
        delegationInfo = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as DelegationInfo?
        delegationInfo?.apply {
            viewBinding {
                etAddress.setText(validator_address)
                amount = shares ?: "0"
                tvMaxAmount.text =
                    "${getString(R.string.undelegateMaxAmount)}${StringUtils.pdip2DIP(amount)}"
            }
        }
        vm?.undelegateLD?.observe(this, Observer {
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
                vm?.undelegate(s, delegationInfo!!)
            },
        ).authenticate()
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

    companion object {
        fun start(context: Context, delegationInfo: DelegationInfo) {
            val intent = Intent(context, UndelegateActivity::class.java)
            intent.putExtra(ExtraKey.SERIALIZABLE, delegationInfo)
            context.startActivity(intent)
        }
    }
}