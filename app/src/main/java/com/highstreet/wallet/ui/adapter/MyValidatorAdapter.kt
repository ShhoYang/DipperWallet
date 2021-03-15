package com.highstreet.wallet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hao.library.adapter.BasePagedAdapter
import com.hao.library.adapter.ViewHolder
import com.highstreet.wallet.databinding.ItemMyValidatorBinding
import com.highstreet.wallet.model.res.Validator

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
class MyValidatorAdapter : BasePagedAdapter<ItemMyValidatorBinding, Validator>() {

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemMyValidatorBinding.inflate(layoutInflater, parent, false)

    override fun bindViewHolder(
        viewHolder: ViewHolder<ItemMyValidatorBinding>,
        item: Validator,
        position: Int,
        payloads: MutableList<Any>
    ) {
        viewHolder.viewBinding {
            tvName.text = item.description?.moniker
            tvDelegationAmount.text = item.delegationInfo?.getDelegationAmount()
            tvUnbondingDelegationAmount.text =
                item.unbondingDelegationInfo?.getUnbondingDelegationAmount()
            tvReward.text = item.reward?.getReward()
        }
    }
}