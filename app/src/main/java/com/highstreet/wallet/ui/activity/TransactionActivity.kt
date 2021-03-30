package com.highstreet.wallet.ui.activity

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.hao.library.view.listener.RxView
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.databinding.ActivityTransactionBinding
import com.highstreet.wallet.extensions.focusListener
import com.highstreet.wallet.extensions.isAddress
import com.highstreet.wallet.extensions.isAmount
import com.highstreet.wallet.extensions.string
import com.highstreet.wallet.fingerprint.FingerprintUtils
import com.highstreet.wallet.ui.vm.TransactionVM

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */
@AndroidEntryPoint
class TransactionActivity : BaseActivity<ActivityTransactionBinding, TransactionVM>() {

    private var amount = ""

    override fun initView() {
        setTitle(R.string.ta_transaction)
        viewBinding {
            etToAddress.focusListener(toAddressLine.line)
            etAmount.focusListener(amountLine.line)
            etMemo.focusListener(memoLine.line)

            RxView.click(ivScan, this@TransactionActivity, ScanActivity.start)
            RxView.click(tvAll) {
                etAmount.setText(amount)
            }
            RxView.click(btnConfirm) {
                transact()
            }
        }
    }


    override fun initData() {
        viewModel {
            amountLD.observe(this@TransactionActivity) {
                it?.apply {
                    val dip = getAmount()
                    amount = dip.substring(0, dip.length - 3)
                    vb?.tvBalance?.text = "${getString(R.string.ta_balance)} ${amount}DIP"
                }
            }
            resultLD.observe(this@TransactionActivity) {
                hideLoading()
                toast(it.second)
                if (it.first) {
                    finish()
                }
            }
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
                    toast(R.string.ta_invalidAddress)
                }
            }
        }
    }

    private fun transact() {
        val address = vb?.etToAddress?.string() ?: ""
        if (!address.isAddress()) {
            toast(R.string.ta_invalidAddress)
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
                    s == amount,
                    vb?.etMemo?.string() ?: ""
                )
            },
        ).authenticate()
    }
}