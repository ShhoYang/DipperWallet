package com.highstreet.wallet.ui.adapter

import com.highstreet.lib.adapter.BasePagedAdapter
import com.highstreet.lib.adapter.ViewHolder
import com.highstreet.wallet.R
import com.highstreet.wallet.model.res.Tx
import com.highstreet.wallet.utils.StringUtils

/**
 * @author Yang Shihao
 * @Date 2020/10/20
 */

class TransactionRecordAdapter(private val isIn: Boolean) :
    BasePagedAdapter<Tx>(R.layout.g_item_common) {

    override fun bindViewHolder(holder: ViewHolder, item: Tx, position: Int) {
        val desc = StringBuilder()
        desc.append(getString(R.string.amount)).append("：")
            .append(StringUtils.pdip2DIP(item.getAmount())).append("\n")
        if (!isIn) {

            desc.append(getString(R.string.fee)).append("：")
                .append(StringUtils.pdip2DIP(item.getFee())).append("\n")
        }
        desc.append(getString(R.string.time)).append("：")
            .append(StringUtils.utc2String(item.timestamp))

        holder.setText(
            R.id.tvTitle,
            "${getString(R.string.fromAddress)}：${if (isIn) item.getFromAddress() else item.getToAddress()}"
        ).setText(R.id.tvDesc, desc)
    }
}