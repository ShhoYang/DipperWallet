package com.highstreet.wallet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hao.library.adapter.BasePagedAdapter
import com.hao.library.adapter.ViewHolder
import com.highstreet.wallet.databinding.ItemValidatorBinding
import com.highstreet.wallet.model.res.Validator
import com.highstreet.wallet.utils.StringUtils

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

class ValidatorAdapter : BasePagedAdapter<ItemValidatorBinding,Validator>() {

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemValidatorBinding.inflate(layoutInflater, parent, false)

    override fun bindViewHolder(
        viewHolder: ViewHolder<ItemValidatorBinding>,
        item: Validator,
        position: Int,
        payloads: MutableList<Any>
    ) {
        viewHolder.viewBinding {
            tvName.text = item.description?.moniker
            tvShares.text = StringUtils.pdip2DIP(item.delegator_shares)
            tvAverageYield.text = item.getRate()
        }
    }
}