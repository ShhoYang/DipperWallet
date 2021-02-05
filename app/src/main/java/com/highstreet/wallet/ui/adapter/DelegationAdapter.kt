package com.highstreet.wallet.ui.adapter

import com.highstreet.lib.adapter.BasePagedAdapter
import com.highstreet.lib.adapter.ViewHolder
import com.highstreet.wallet.R
import com.highstreet.wallet.model.res.DelegationInfo
import com.highstreet.wallet.utils.StringUtils

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

class DelegationAdapter  : BasePagedAdapter<DelegationInfo>(R.layout.g_item_common) {

    override fun bindViewHolder(holder: ViewHolder, item: DelegationInfo, position: Int) {
        val desc = StringBuilder()
        desc.append(getString(R.string.initAmount)).append("：").append(StringUtils.pdip2DIP(item.shares)).append("\n")
            .append(getString(R.string.balance)).append("：").append(StringUtils.pdip2DIP(item.balance))
        holder.setText(R.id.tvTitle, "${getString(R.string.validatorAddress)}：${item.validator_address}")
            .setText(R.id.tvDesc, desc)
    }
}