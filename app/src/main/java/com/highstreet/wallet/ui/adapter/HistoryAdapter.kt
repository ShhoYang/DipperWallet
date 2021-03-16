package com.highstreet.wallet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hao.library.adapter.BasePagedAdapter
import com.hao.library.adapter.ViewHolder
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.Colors
import com.highstreet.wallet.databinding.ItemHistoryBinding
import com.highstreet.wallet.model.res.Tx

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
class HistoryAdapter : BasePagedAdapter<ItemHistoryBinding, Tx>() {

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemHistoryBinding.inflate(layoutInflater, parent, false)

    override fun bindViewHolder(
        viewHolder: ViewHolder<ItemHistoryBinding>,
        item: Tx,
        position: Int,
        payloads: MutableList<Any>
    ) {

        viewHolder.viewBinding {
            val context = root.context
            if (item.success()) {
                tvState.setTextColor(Colors.textGreen)
                tvState.text = context.getString(R.string.succeed)

            } else {
                tvState.setTextColor(Colors.textRed)
                tvState.text = context.getString(R.string.failed)
            }
            tvType.text = item.getType()

            tvTime.text = item.getTime()
            tvBlockHeight.text = "${item.height} block"
        }
    }
}