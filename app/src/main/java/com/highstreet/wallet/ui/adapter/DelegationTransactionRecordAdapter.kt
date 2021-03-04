package com.highstreet.wallet.ui.adapter

import com.hao.library.adapter.ViewHolder
import com.highstreet.wallet.R
import com.highstreet.wallet.databinding.ItemCommonBinding
import com.highstreet.wallet.model.res.Tx
import com.highstreet.wallet.utils.StringUtils

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

class DelegationTransactionRecordAdapter : CommonAdapter<Tx>() {

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
                .append(StringUtils.pdip2DIP(item.getAmount())).append("\n")
                .append(context.getString(R.string.time)).append("：")
                .append(StringUtils.utc2String(item.timestamp))

            tvTitle.text =
                "${context.getString(R.string.validatorAddress)}：${item.getValidatorAddress()}"
            tvDesc.text = desc
        }
    }

}