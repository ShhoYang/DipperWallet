package com.highstreet.wallet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hao.library.adapter.BasePagedAdapter
import com.hao.library.adapter.ViewHolder
import com.highstreet.wallet.databinding.ItemValidatorChooseBinding
import com.highstreet.wallet.model.res.Validator
import com.highstreet.wallet.utils.StringUtils

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

class ValidatorChooseAdapter : BasePagedAdapter<ItemValidatorChooseBinding, Validator>() {

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemValidatorChooseBinding.inflate(layoutInflater, parent, false)

    override fun bindViewHolder(
        viewHolder: ViewHolder<ItemValidatorChooseBinding>,
        item: Validator,
        position: Int,
        payloads: MutableList<Any>
    ) {
        viewHolder.viewBinding {
            tvAvatar.text = item.getFirstLetterName()
            tvName.text = item.description?.moniker
            tvShares.text = StringUtils.formatDecimal(item.delegator_shares)
            tvAddress.text = item.operator_address
            tvRate.text = item.getRate()
            ivArrow.setOnClickListener {
                itemClickListener?.itemClicked(it, item, position)
            }
        }
    }
}