package com.highstreet.wallet.ui.activity

import android.content.Context
import android.content.Intent
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.hao.library.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.databinding.ActivityTokenDetailBinding
import com.highstreet.wallet.db.Token
import com.highstreet.wallet.ui.vm.TokenDetailVM
import com.highstreet.wallet.view.QRDialog

/**
 * @author Yang Shihao
 * @Date 3/5/21
 */
@AndroidEntryPoint
class TokenDetailActivity :
    BaseActivity<ActivityTokenDetailBinding, TokenDetailVM>() {

    private var token: Token? = null
    private var amount = ""

    override fun initView() {
        setTitle(R.string.detail)
    }

    override fun initData() {
        token = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as Token?

        if (token == null) {
            finish()
            return
        }
        viewBinding {
            RxView.click(ivWalletAddress, this@TokenDetailActivity::showQr)
            RxView.click(btnSend, this@TokenDetailActivity, token!!, TokenTransactionActivity::start)
        }
        viewModel {
            amountLD.observe(this@TokenDetailActivity) {
                amount = it
                updateAmount()
            }
            loadData(token!!.address)
        }
    }

    private fun updateAmount() {
        viewBinding {
            tvWalletAddress.text = token?.address
            tvAmount.text = amount
            tvAvailable.text = amount
//            tvAmountValue.text = AmountUtils.getAmountValue(amount)
        }
    }

    private fun showQr() {
        token?.let {
            QRDialog(this).show(it.name, it.address)
        }
    }

    companion object {
        fun start(context: Context, token: Token) {
            val intent = Intent(context, TokenDetailActivity::class.java)
            intent.putExtra(ExtraKey.SERIALIZABLE, token)
            context.startActivity(intent)
        }
    }
}