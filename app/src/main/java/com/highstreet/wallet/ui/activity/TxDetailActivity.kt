package com.highstreet.wallet.ui.activity

import android.content.Context
import android.content.Intent
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.Colors
import com.highstreet.wallet.constant.ExtraKey
import com.highstreet.wallet.databinding.ActivityTxDetailBinding
import com.highstreet.wallet.model.res.Tx
import com.highstreet.wallet.utils.StringUtils

/**
 * @author Yang Shihao
 * @Date 3/16/21
 */
@AndroidEntryPoint(injectViewModel = false)
class TxDetailActivity : BaseActivity<ActivityTxDetailBinding, PlaceholderViewModel>() {

    override fun initView() {
        setTitle(R.string.transactionDetail)
    }

    override fun initData() {
        val tx = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as Tx?
        tx?.let {
            viewBinding {
                if (it.success()) {
                    statePoint.setBackgroundResource(R.drawable.shape_circle_green)
                    tvState.text = getString(R.string.succeed)

                } else {
                    statePoint.setBackgroundResource(R.drawable.shape_circle_red)
                    tvState.text = getString(R.string.failed)
                }

                tvBlockHeight.text = "${it.height}"
                tvFee.text = it.getFee()
                tvTime.text = it.getTime()
                tvTransactionHash.text = it.txhash
                tvMemo.text = it?.tx?.value?.memo
            }
        }
    }

    companion object {
        fun start(context: Context, tx: Tx) {
            val intent = Intent(context, TxDetailActivity::class.java)
            intent.putExtra(ExtraKey.SERIALIZABLE, tx)
            context.startActivity(intent)
        }
    }
}