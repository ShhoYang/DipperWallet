package com.highstreet.wallet.ui.activity

import android.app.Activity
import androidx.lifecycle.Observer
import android.content.Intent
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.highstreet.lib.extensions.string
import com.highstreet.lib.fingerprint.FingerprintUtils
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.utils.StringUtils
import com.highstreet.wallet.constant.Colors
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.extensions.isAddress
import com.highstreet.wallet.extensions.isAmount
import com.highstreet.wallet.ui.vm.TransactionVM
import kotlinx.android.synthetic.main.g_activity_transaction.*

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */

class TransactionActivity : BaseActivity(), View.OnClickListener, View.OnFocusChangeListener {


    private var longAmount = 0L
    private var amount = ""

    override fun showToolbar() = false

    private val viewModel by lazy {
        ViewModelProvider(this).get(TransactionVM::class.java)
    }

    override fun getLayoutId() = R.layout.g_activity_transaction

    override fun initView() {
        setTitle(R.string.transaction)
        etToAddress.onFocusChangeListener = this
        etAmount.onFocusChangeListener = this
        etRemarks.onFocusChangeListener = this

        RxView.textChanges(etAmount) {
            btnConfirm.isEnabled = etAmount.string().isNotEmpty()
        }
        RxView.click(ivScan, this)
        RxView.click(btnConfirm, this)
        RxView.click(tvAll, this)
    }

    private fun transact() {

        val address = etToAddress.string()
        if (!address.isAddress()) {
            toast(R.string.invalidAddress)
            return
        }
        val s = etAmount.string()
        if (TextUtils.isEmpty(s) || !s.isAmount()) {
            toast(R.string.amountFormatError)
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
        viewModel.amountLD.observe(this, Observer {
            it?.apply {
                longAmount = getLongAmount()
                val dip = StringUtils.pdip2DIP(getAmount())
                amount = dip.substring(0, dip.length - 3)
                tvBalance.text = "${getString(R.string.balance)} ${amount}DIP"
            }
        })
        viewModel.resultLD.observe(this, Observer {
            hideLoading()
            toast(it.second)
            if (it.first) {
                finish()
            }
        })
        viewModel.getAccountInfo(AccountManager.instance().address)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ScanActivity.REQUEST_CODE_SCAN && resultCode == Activity.RESULT_OK) {
            val address = data?.getStringExtra(ExtraKey.STRING)

            if (null !== address) {
                if (address.isAddress()) {
                    etToAddress.setText(address)
                } else {
                    toast(R.string.invalidAddress)
                }
            }
        }
    }

    override fun onFingerprintAuthenticateSucceed() {
        showLoading()
        viewModel.transact(
            etToAddress.string(),
            etAmount.string(),
            longAmount,
            etAmount.string() == amount,
            etRemarks.string()
        )
    }

    override fun usePassword(password: String): Boolean {
        if (!AccountManager.instance().password!!.verify(password)) {
            return false
        }
        onFingerprintAuthenticateSucceed()
        return true
    }

    override fun onClick(v: View?) {
        when (v) {
            ivScan -> ScanActivity.start(this)
            btnConfirm -> transact()
            tvAll -> etAmount.setText(amount)

        }
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        when (v) {
            etToAddress -> updateLineStyle(toAddressLine, hasFocus)
            etAmount -> updateLineStyle(amountLine, hasFocus)
            etRemarks -> updateLineStyle(remarksLine, hasFocus)
        }
    }
}