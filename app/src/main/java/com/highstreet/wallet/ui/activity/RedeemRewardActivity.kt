package com.highstreet.wallet.ui.activity

import android.content.Context
import android.content.Intent
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.hao.library.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.databinding.ActivityRedeemRewardBinding
import com.highstreet.wallet.extensions.focusListener
import com.highstreet.wallet.extensions.isAddress
import com.highstreet.wallet.extensions.string
import com.highstreet.wallet.fingerprint.FingerprintUtils
import com.highstreet.wallet.ui.vm.ReceiveRewardVM

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 *
 * 领取奖励
 */
@AndroidEntryPoint
class RedeemRewardActivity : BaseActivity<ActivityRedeemRewardBinding, ReceiveRewardVM>() {


    override fun initView() {
        setTitle(R.string.rra_receiveReward)
        viewBinding {
            etReceiveAddress.focusListener(receiveAddressLine.line)
            RxView.textChanges(etReceiveAddress) {
                btnConfirm.isEnabled = etReceiveAddress.string().isNotEmpty()
            }
            RxView.click(btnConfirm, this@RedeemRewardActivity::receive)
        }
    }

    private fun receive() {
        val validatorAddress = vb?.etValidatorAddress?.string() ?: ""
        if (!validatorAddress.isAddress()) {
            toast(R.string.rra_invalidValidatorAddress)
            return
        }
        val receiveAddress = vb?.etReceiveAddress?.string() ?: ""
        if (!receiveAddress.isAddress()) {
            toast(R.string.rra_invalidReceiveAddress)
            return
        }

        FingerprintUtils.getFingerprint(
            this,
            null,
            true,
            {
                showLoading()
                vm?.receiveReward(validatorAddress, receiveAddress)
            },
        ).authenticate()
    }

    override fun initData() {
        val data = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as ArrayList<String>?
        if (data != null && data.size == 3) {
            viewBinding {
                etValidatorAddress.setText(data[0])
                etReceiveAddress.setText(data[1])
                etAmount.setText(data[2])
            }
        }
        vm?.resultLD?.observe(this) {
            hideLoading()
            toast(it.second)
            if (it.first) {
                toA(MainActivity::class.java)
            }
        }
    }

    companion object {
        fun start(
            context: Context,
            validatorAddress: String,
            delegatorAddress: String,
            reword: String
        ) {
            val intent = Intent(context, RedeemRewardActivity::class.java)
            intent.putExtra(
                ExtraKey.SERIALIZABLE,
                arrayListOf(validatorAddress, delegatorAddress, reword)
            )
            context.startActivity(intent)
        }
    }
}