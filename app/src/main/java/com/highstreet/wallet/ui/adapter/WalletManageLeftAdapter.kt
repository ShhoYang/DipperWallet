package com.highstreet.wallet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hao.library.adapter.BaseMultiTypeNormalAdapter
import com.hao.library.adapter.ItemViewDelegate
import com.hao.library.adapter.ViewHolder
import com.highstreet.wallet.databinding.ItemWalletManageLeftAllBinding
import com.highstreet.wallet.databinding.ItemWalletManageLeftBinding
import com.highstreet.wallet.model.WalletType

/**
 * @author Yang Shihao
 * @Date 2020/10/22
 */

class WalletManageLeftAdapter : BaseMultiTypeNormalAdapter<WalletType>() {

    init {
        addDelegate(Type())
        addDelegate(All())
    }

    private inner class Type : ItemViewDelegate<ItemWalletManageLeftBinding, WalletType> {

        override fun isViewType(item: WalletType, position: Int) = !item.isAll

        override fun getViewBinding(
            layoutInflater: LayoutInflater,
            parent: ViewGroup
        ) = ItemWalletManageLeftBinding.inflate(layoutInflater, parent, false)

        override fun bindViewHolder(
            viewHolder: ViewHolder<ItemWalletManageLeftBinding>,
            item: WalletType,
            position: Int,
            payloads: MutableList<Any>
        ) {
            viewHolder.viewBinding {
                ivChainIcon.setImageResource(item.chainIcon)
                tvChainName.text = item.chain.showName
                root.isSelected = item.selected
                root.setOnClickListener {
                    itemClickListener?.itemClicked(it, item, position)
                }
            }
        }
    }

    private inner class All : ItemViewDelegate<ItemWalletManageLeftAllBinding, WalletType> {

        override fun isViewType(item: WalletType, position: Int) = item.isAll

        override fun getViewBinding(
            layoutInflater: LayoutInflater,
            parent: ViewGroup
        ) = ItemWalletManageLeftAllBinding.inflate(layoutInflater, parent, false)

        override fun bindViewHolder(
            viewHolder: ViewHolder<ItemWalletManageLeftAllBinding>,
            item: WalletType,
            position: Int,
            payloads: MutableList<Any>
        ) {
            viewHolder.viewBinding {
                tvChainName.text = item.chain.showName
                root.isSelected = item.selected
                root.setOnClickListener {
                    itemClickListener?.itemClicked(it, item, position)
                }
            }
        }
    }
}