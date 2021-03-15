package com.highstreet.wallet.ui.adapter

import com.hao.library.adapter.ViewHolder
import com.highstreet.wallet.R
import com.highstreet.wallet.databinding.ItemCommonBinding
import com.highstreet.wallet.model.res.DelegationInfo
import com.highstreet.wallet.utils.StringUtils

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

class DelegationAdapter : CommonAdapter<DelegationInfo>() {

    override fun bindViewHolder(
        viewHolder: ViewHolder<ItemCommonBinding>,
        item: DelegationInfo,
        position: Int,
        payloads: MutableList<Any>
    ) {
        viewHolder.viewBinding {
            val context = root.context
            val desc = StringBuilder()
            desc.append(context.getString(R.string.initAmount)).append("：")
                .append(item.getDelegationAmount()).append("\n")
                .append(context.getString(R.string.balance)).append("：")
                .append(item.getBalance())

            tvTitle.text =
                "${context.getString(R.string.validatorAddress)}：${item.validator_address}"
            tvDesc.text = desc

        }
    }
}