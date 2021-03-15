package com.highstreet.wallet.ui.activity

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.databinding.ActivityTransactionBinding
import com.highstreet.wallet.extensions.isAddress
import com.highstreet.wallet.extensions.isAmount
import com.highstreet.wallet.extensions.string
import com.highstreet.wallet.fingerprint.FingerprintUtils
import com.highstreet.wallet.ui.vm.TransactionVM
import com.highstreet.wallet.utils.StringUtils
import com.highstreet.wallet.utils.ViewUtils
import com.highstreet.wallet.view.listener.RxView

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */
@AndroidEntryPoint
class TransactionActivity : BaseActivity<ActivityTransactionBinding, TransactionVM>(),
    View.OnClickListener, View.OnFocusChangeListener {


    private var longAmount = 0L
    private var amount = ""

    override fun initView() {
        setTitle(R.string.transaction)
        viewBinding {
            etToAddress.onFocusChangeListener = this@TransactionActivity
            etAmount.onFocusChangeListener = this@TransactionActivity
            etRemarks.onFocusChangeListener = this@TransactionActivity

            RxView.click(ivScan, this@TransactionActivity)
            RxView.click(btnConfirm, this@TransactionActivity)
            RxView.click(tvAll, this@TransactionActivity)
        }
    }

    private fun transact() {
        val address = vb?.etToAddress?.string() ?: ""
        if (!address.isAddress()) {
            toast(R.string.invalidAddress)
            return
        }
        val s = vb?.etAmount?.string() ?: ""
        if (TextUtils.isEmpty(s) || !s.isAmount()) {
            toast(R.string.amountFormatError)
            return
        }

        FingerprintUtils.getFingerprint(
            this,
            null,
            true,
            {
                showLoading()
                vm?.transact(
                    address,
                    s,
                    longAmount,
                    s == amount,
                    vb?.etRemarks?.string() ?: ""
                )
            },
        ).authenticate()
    }

    override fun initData() {
        viewModel {
            amountLD.observe(this@TransactionActivity, Observer {
                it?.apply {
                    longAmount = getLongAmount()
                    val dip = getAmount()
                    amount = dip.substring(0, dip.length - 3)
                    vb?.tvBalance?.text = "${getString(R.string.balance)} ${amount}DIP"
                }
            })
            resultLD.observe(this@TransactionActivity, Observer {
                hideLoading()
                toast(it.second)
                if (it.first) {
                    finish()
                }
            })
            getAccountInfo(AccountManager.instance().address)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ScanActivity.REQUEST_CODE_SCAN && resultCode == Activity.RESULT_OK) {
            val address = data?.getStringExtra(ExtraKey.STRING)

            if (null !== address) {
                if (address.isAddress()) {
                    vb?.etToAddress?.setText(address)
                } else {
                    toast(R.string.invalidAddress)
                }
            }
        }
    }

    override fun onClick(v: View?) {
        viewBinding {
            when (v) {
                ivScan -> ScanActivity.start(this@TransactionActivity)
                btnConfirm -> transact()
                tvAll -> etAmount.setText(amount)
            }
        }
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        viewBinding {
            when (v) {
                etToAddress -> ViewUtils.updateLineStyle(toAddressLine.line, hasFocus)
                etAmount -> ViewUtils.updateLineStyle(amountLine.line, hasFocus)
                etRemarks -> ViewUtils.updateLineStyle(remarksLine.line, hasFocus)
            }
        }
    }
}