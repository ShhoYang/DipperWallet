package com.highstreet.wallet.ui.activity

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.Colors
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.databinding.ActivityRedeemRewardBinding
import com.highstreet.wallet.extensions.isAddress
import com.highstreet.wallet.extensions.string
import com.highstreet.wallet.fingerprint.FingerprintUtils
import com.highstreet.wallet.ui.vm.ReceiveRewardVM
import com.highstreet.wallet.view.listener.RxView

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 *
 * 领取奖励
 */
@AndroidEntryPoint
class RedeemRewardActivity : BaseActivity<ActivityRedeemRewardBinding, ReceiveRewardVM>(),
    View.OnFocusChangeListener {


    override fun initView() {
        setTitle(R.string.receiveReward)
        viewBinding {
            etReceiveAddress.onFocusChangeListener = this@RedeemRewardActivity
            RxView.textChanges(etReceiveAddress) {
                btnConfirm.isEnabled = etReceiveAddress.string().isNotEmpty()
            }
            RxView.click(btnConfirm) {
                receive()
            }
        }
    }

    private fun receive() {
        val validatorAddress = vb?.etValidatorAddress?.string() ?: ""
        if (!validatorAddress.isAddress()) {
            toast(R.string.invalidValidatorAddress)
            return
        }
        val receiveAddress = vb?.etReceiveAddress?.string() ?: ""
        if (!receiveAddress.isAddress()) {
            toast(R.string.invalidReceiveAddress)
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

    private fun updateLineStyle(view: View, hasFocus: Boolean) {
        view.setBackgroundColor(if (hasFocus) Colors.editLineFocus else Colors.editLineBlur)
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
        vm?.resultLD?.observe(this, Observer {
            hideLoading()

            if (it.first) {
                toast(R.string.succeed)
                toA(MainActivity::class.java)
            } else {
                toast(R.string.failed)
            }
        })
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        viewBinding {
            when (v) {
                etReceiveAddress -> updateLineStyle(receiveAddressLine.line, hasFocus)
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