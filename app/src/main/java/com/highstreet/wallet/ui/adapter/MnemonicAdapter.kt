package com.highstreet.wallet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hao.library.adapter.BaseNormalAdapter
import com.hao.library.adapter.ViewHolder
import com.highstreet.wallet.databinding.ItemMnemonicBinding

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */

class MnemonicAdapter : BaseNormalAdapter<ItemMnemonicBinding, String>() {

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemMnemonicBinding.inflate(layoutInflater, parent, false)

    override fun bindViewHolder(
        viewHolder: ViewHolder<ItemMnemonicBinding>,
        item: String,
        position: Int,
        payloads: MutableList<Any>
    ) {
        viewHolder.viewBinding {
            root.text = item
        }
    }
}