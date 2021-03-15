package com.highstreet.wallet.ui.adapter

import com.hao.library.adapter.ViewHolder
import com.highstreet.wallet.R
import com.highstreet.wallet.databinding.ItemCommonBinding
import com.highstreet.wallet.model.res.Tx
import com.highstreet.wallet.utils.StringUtils

/**
 * @author Yang Shihao
 * @Date 2020/10/20
 */

class TransactionRecordAdapter : CommonAdapter<Tx>() {

    val isIn: Boolean = true

    override fun bindViewHolder(
        viewHolder: ViewHolder<ItemCommonBinding>,
        item: Tx,
        position: Int,
        payloads: MutableList<Any>
    ) {
        viewHolder.viewBinding {
            val context = root.context
            val desc = StringBuilder()
            desc.append(context.getString(R.string.amount)).append("：")
                .append(item.getAmount()).append("\n")
            if (!isIn) {

                desc.append(context.getString(R.string.fee)).append("：")
                    .append(item.getFee()).append("\n")
            }
            desc.append(context.getString(R.string.time)).append("：")
                .append(StringUtils.utc2String(item.timestamp))

            tvTitle.text =
                "${context.getString(R.string.fromAddress)}：${if (isIn) item.getFromAddress() else item.getToAddress()}"
            tvDesc.text = desc
        }
    }
}