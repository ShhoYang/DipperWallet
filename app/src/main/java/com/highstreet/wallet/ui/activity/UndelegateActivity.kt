package com.highstreet.wallet.ui.activity

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.hao.library.AppManager
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.hao.library.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.databinding.ActivityUndelegateBinding
import com.highstreet.wallet.extensions.focusListener
import com.highstreet.wallet.extensions.isAmount
import com.highstreet.wallet.extensions.string
import com.highstreet.wallet.fingerprint.FingerprintUtils
import com.highstreet.wallet.model.res.DelegationInfo
import com.highstreet.wallet.ui.vm.UndelegateVM
import com.highstreet.wallet.utils.AmountUtils

/**
 * @author Yang Shihao
 * @Date 2020/10/27
 */
@AndroidEntryPoint
class UndelegateActivity : BaseActivity<ActivityUndelegateBinding, UndelegateVM>() {

    private var amount = "0"

    private var delegationInfo: DelegationInfo? = null

    override fun initView() {
        setTitle(R.string.ua_undelegate)
        viewBinding {
            etAmount.focusListener(amountLine.line)
            etMemo.focusListener(memoLine.line)

            RxView.textChanges(etAmount) {
                btnConfirm.isEnabled = etAmount.string().isNotEmpty()
            }

            RxView.click(tvAll) {
                etAmount.setText(amount)
                etAmount.setSelection(etAmount.string().length)
            }

            RxView.click(btnConfirm, this@UndelegateActivity::unDelegate)
        }
    }

    override fun initData() {
        delegationInfo = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as DelegationInfo?
        delegationInfo?.apply {
            viewBinding {
                etAddress.setText(validator_address)
                amount = shares ?: "0"
                tvMaxAmount.text =
                    "${getString(R.string.ua_undelegateMaxAmount)}${AmountUtils.pdip2DIP(amount)}"
            }
        }
        vm?.undelegateLD?.observe(this) {
            hideLoading()
            toast(it.second)
            if (it.first) {
                AppManager.instance().finishActivity(DelegationDetailActivity::class.java)
                finish()
            }
        }
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
            toast(R.string.ua_overMaxAmount)
            return
        }

        FingerprintUtils.getFingerprint(
            this,
            null,
            true,
            {
                showLoading()
                vm?.undelegate(s, delegationInfo!!, vb?.etMemo?.string() ?: "")
            },
        ).authenticate()
    }

    companion object {
        fun start(context: Context, delegationInfo: DelegationInfo) {
            val intent = Intent(context, UndelegateActivity::class.java)
            intent.putExtra(ExtraKey.SERIALIZABLE, delegationInfo)
            context.startActivity(intent)
        }
    }
}