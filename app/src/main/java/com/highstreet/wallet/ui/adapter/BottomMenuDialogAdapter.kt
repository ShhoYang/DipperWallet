package com.highstreet.wallet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hao.library.adapter.BaseNormalAdapter
import com.hao.library.adapter.ViewHolder
import com.highstreet.wallet.databinding.DialogBottomMenuItemBinding
import com.highstreet.wallet.view.IOptionItem

/**
 * @author Yang Shihao
 * @Date 3/6/21
 */
class BottomMenuDialogAdapter<T : IOptionItem> :
    BaseNormalAdapter<DialogBottomMenuItemBinding, T>() {

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ) = DialogBottomMenuItemBinding.inflate(layoutInflater, parent, false)

    override fun bindViewHolder(
        viewHolder: ViewHolder<DialogBottomMenuItemBinding>,
        item: T,
        position: Int,
        payloads: MutableList<Any>
    ) {
        viewHolder.viewBinding {
            tvText.text = item.getShowText()
        }
    }
}