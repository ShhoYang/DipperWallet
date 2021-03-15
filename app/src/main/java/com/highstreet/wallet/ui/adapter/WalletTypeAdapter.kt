package com.highstreet.wallet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hao.library.adapter.BaseNormalAdapter
import com.hao.library.adapter.ViewHolder
import com.highstreet.wallet.databinding.ItemWalletTypeBinding
import com.highstreet.wallet.model.WalletType

/**
 * @author Yang Shihao
 * @Date 2020/10/22
 */

class WalletTypeAdapter : BaseNormalAdapter<ItemWalletTypeBinding, WalletType>() {

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemWalletTypeBinding.inflate(layoutInflater, parent, false)

    override fun bindViewHolder(
        viewHolder: ViewHolder<ItemWalletTypeBinding>,
        item: WalletType,
        position: Int,
        payloads: MutableList<Any>
    ) {
        viewHolder.viewBinding {
            ivChainIcon.setImageResource(item.chainIcon)
            tvChainName.text = item.chain.chainName.replace("_", "")
        }
    }
}