package com.highstreet.wallet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hao.library.adapter.BaseMultiTypeNormalAdapter
import com.hao.library.adapter.ItemViewDelegate
import com.hao.library.adapter.ViewHolder
import com.highstreet.wallet.databinding.*
import com.highstreet.wallet.model.Menu

/**
 * @author Yang Shihao
 * @Date 3/6/21
 */
class SettingAdapter : BaseMultiTypeNormalAdapter<Menu>() {

    init {
        addDelegate(NormalItem())
        addDelegate(GroupItem())
        addDelegate(SpaceItem())
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
                tvLeft.text = item.title
                tvRight.text = item.desc
            }
        }
    }

    inner class GroupItem : ItemViewDelegate<ItemMenuGroupBinding, Menu> {

        override fun isViewType(item: Menu, position: Int) = item.type == Menu.TYPE_GROUP

        override fun getViewBinding(
            layoutInflater: LayoutInflater,
            parent: ViewGroup
        ) = ItemMenuGroupBinding.inflate(layoutInflater, parent, false)

        override fun bindViewHolder(
            viewHolder: ViewHolder<ItemMenuGroupBinding>,
            item: Menu,
            position: Int,
            payloads: MutableList<Any>
        ) {
            viewHolder.viewBinding {
                root.text = item.title
            }
        }
    }

    inner class SpaceItem : ItemViewDelegate<ItemMenuSpaceBinding, Menu> {

        override fun isViewType(item: Menu, position: Int) = item.type == Menu.TYPE_SPACE

        override fun getViewBinding(
            layoutInflater: LayoutInflater,
            parent: ViewGroup
        ) = ItemMenuSpaceBinding.inflate(layoutInflater, parent, false)

        override fun bindViewHolder(
            viewHolder: ViewHolder<ItemMenuSpaceBinding>,
            item: Menu,
            position: Int,
            payloads: MutableList<Any>
        ) {
        }
    }
}


