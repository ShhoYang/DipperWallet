package com.highstreet.wallet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hao.library.adapter.BaseMultiTypeNormalAdapter
import com.hao.library.adapter.ItemViewDelegate
import com.hao.library.adapter.ViewHolder
import com.hao.library.extensions.load
import com.highstreet.wallet.databinding.ItemMenuBinding
import com.highstreet.wallet.databinding.ItemMenuNarrowLineBinding
import com.highstreet.wallet.databinding.ItemMenuWideLineBinding
import com.highstreet.wallet.model.Menu

/**
 * @author Yang Shihao
 * @date 2020/10/20
 */
class MenuAdapter : BaseMultiTypeNormalAdapter<Menu>() {

    init {
        addDelegate(NormalItem())
        addDelegate(WideLineItem())
        addDelegate(NarrowLineItem())
    }

    inner class NormalItem : ItemViewDelegate<ItemMenuBinding, Menu> {

        override fun isViewType(item: Menu, position: Int) = item.type == Menu.TYPE_NORMAL

        override fun getViewBinding(
            layoutInflater: LayoutInflater,
            parent: ViewGroup
        ) = ItemMenuBinding.inflate(layoutInflater, parent, false)

        override fun bindViewHolder(
            viewHolder: ViewHolder<ItemMenuBinding>,
            item: Menu,
            position: Int,
            payloads: MutableList<Any>
        ) {
            viewHolder.viewBinding {
                item.icon?.let {
                    ivIcon.load(it)
                }
                tvText.text = item.title
            }
        }
    }

    inner class WideLineItem : ItemViewDelegate<ItemMenuWideLineBinding, Menu> {

        override fun isViewType(item: Menu, position: Int) = item.type == Menu.TYPE_WIDE_LINE

        override fun getViewBinding(
            layoutInflater: LayoutInflater,
            parent: ViewGroup
        ) = ItemMenuWideLineBinding.inflate(layoutInflater, parent, false)

        override fun bindViewHolder(
            viewHolder: ViewHolder<ItemMenuWideLineBinding>,
            item: Menu,
            position: Int,
            payloads: MutableList<Any>
        ) {
        }
    }

    inner class NarrowLineItem : ItemViewDelegate<ItemMenuNarrowLineBinding, Menu> {

        override fun isViewType(item: Menu, position: Int) = item.type == Menu.TYPE_NARROW_LINE

        override fun getViewBinding(
            layoutInflater: LayoutInflater,
            parent: ViewGroup
        ) = ItemMenuNarrowLineBinding.inflate(layoutInflater, parent, false)

        override fun bindViewHolder(
            viewHolder: ViewHolder<ItemMenuNarrowLineBinding>,
            item: Menu,
            position: Int,
            payloads: MutableList<Any>
        ) {
        }
    }
}


