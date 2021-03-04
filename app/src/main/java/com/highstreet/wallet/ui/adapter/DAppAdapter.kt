package com.highstreet.wallet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hao.library.adapter.BaseNormalAdapter
import com.hao.library.adapter.ViewHolder
import com.hao.library.extensions.load
import com.highstreet.wallet.constant.Icons
import com.highstreet.wallet.databinding.ItemDappBinding
import com.highstreet.wallet.model.Menu

/**
 * @author Yang Shihao
 * @Date 2/25/21
 */
class DAppAdapter : BaseNormalAdapter<ItemDappBinding, Menu>() {

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemDappBinding.inflate(layoutInflater, parent, false)

    override fun bindViewHolder(
        viewHolder: ViewHolder<ItemDappBinding>,
        item: Menu,
        position: Int,
        payloads: MutableList<Any>
    ) {
        viewHolder.viewBinding {
            ivIcon.load(Icons.get(position))
            tvName.text = item.title
        }
    }
}