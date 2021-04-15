package com.highstreet.wallet.ui.activity

import android.content.Context
import android.content.Intent
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.hao.library.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.databinding.ActivityNativeTokenDetailBinding
import com.highstreet.wallet.db.Token
import com.highstreet.wallet.ui.vm.NativeTokenDetailVM
import com.highstreet.wallet.utils.AmountUtils
import com.highstreet.wallet.view.QRDialog

/**
 * @author Yang Shihao
 * @Date 3/5/21
 */
@AndroidEntryPoint
class NativeTokenDetailActivity :
    BaseActivity<ActivityNativeTokenDetailBinding, NativeTokenDetailVM>() {

    private var token: Token? = null

    private var amount = ""

    override fun initView() {
        setTitle(R.string.detail)
        viewBinding {
            RxView.click(ivWalletAddress, this@NativeTokenDetailActivity::showQr)
            RxView.click(
                btnSend,
                TransactionActivity::class.java,
                this@NativeTokenDetailActivity::toA
            )
        }
    }

    override fun initData() {
        token = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as Token?

        if (token == null) {
            finish()
            return
        }
        viewModel {
            amountLD.observe(this@NativeTokenDetailActivity) {
                amount = it?.getAmount() ?: AmountUtils.ZERO
                updateAmount()
            }
            delegationAmountLD.observe(this@NativeTokenDetailActivity) {
                vb?.tvDelagated?.text = it
            }
            unbondingDelegationAmountLD.observe(this@NativeTokenDetailActivity) {
                vb?.tvUnbondingDelegationAmount?.text = it
            }
            rewardLD.observe(this@NativeTokenDetailActivity) {
                vb?.tvReward?.text = it
            }

            loadData(token!!.address)
        }
    }

    private fun updateAmount() {
        viewBinding {
            tvWalletAddress.text = token?.address
            tvAmount.text = amount
            tvAvailable.text = amount
            tvAmountValue.text = AmountUtils.getAmountValue(amount)
        }
    }

    private fun showQr() {
        token?.let {
            QRDialog(this).show(it.name, it.address)
        }
    }

    companion object {
        fun start(context: Context, token: Token) {
            val intent = Intent(context, NativeTokenDetailActivity::class.java)
            intent.putExtra(ExtraKey.SERIALIZABLE, token)
            context.startActivity(intent)
        }
    }
}