package com.highstreet.wallet.ui.activity

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.hao.library.AppManager
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.hao.library.view.listener.RxView
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.databinding.ActivityDelegateBinding
import com.highstreet.wallet.extensions.focusListener
import com.highstreet.wallet.extensions.isAmount
import com.highstreet.wallet.extensions.string
import com.highstreet.wallet.fingerprint.FingerprintUtils
import com.highstreet.wallet.model.res.Validator
import com.highstreet.wallet.ui.vm.DelegateVM

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
@AndroidEntryPoint
class DelegateActivity : BaseActivity<ActivityDelegateBinding, DelegateVM>() {

    private var amount = 0L

    private var validator: Validator? = null

    override fun initView() {
        setTitle(R.string.da_delegation)
        viewBinding {
            etAmount.focusListener(amountLine.line)
            etMemo.focusListener(memoLine.line)

            RxView.textChanges(etAmount) {
                btnConfirm.isEnabled = etAmount.string().isNotEmpty()
            }

            RxView.click(btnConfirm, this@DelegateActivity::delegate)
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
                vm!!.delegate(vb!!.etAddress.string(), s, vb!!.etMemo.string())
            }
        ).authenticate()
    }

    override fun initData() {
        validator = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as Validator?
        validator?.apply {
            vb!!.etAddress.setText(operator_address)
        }

        viewModel {
            amountLD.observe(this@DelegateActivity) {
                it?.apply {
                    amount = getLongAmount()
                    vb!!.tvBalance.text =
                        "${getString(R.string.da_availableBalance)}${getAmount()}"
                }
            }
            resultLD.observe(this@DelegateActivity) {
                hideLoading()
                toast(it.second)
                if (it.first) {
                    AppManager.instance().finishActivity(DelegationDetailActivity::class.java)
                    finish()
                }
            }
            getAccountInfo(AccountManager.instance().address)
        }
    }

    companion object {
        fun start(context: Context, validator: Validator) {
            val intent = Intent(context, DelegateActivity::class.java)
            intent.putExtra(ExtraKey.SERIALIZABLE, validator)
            context.startActivity(intent)
        }
    }
}