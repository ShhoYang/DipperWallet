package com.highstreet.wallet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hao.library.adapter.BaseNormalAdapter
import com.hao.library.adapter.ViewHolder
import com.highstreet.wallet.databinding.DialogBottomMenuItemBinding

/**
 * @author Yang Shihao
 * @Date 3/6/21
 */
class BottomMenuDialogAdapter : BaseNormalAdapter<DialogBottomMenuItemBinding, String>() {

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ) = DialogBottomMenuItemBinding.inflate(layoutInflater, parent, false)

    override fun bindViewHolder(
        viewHolder: ViewHolder<DialogBottomMenuItemBinding>,
        item: String,
        position: Int,
        payloads: MutableList<Any>
    ) {
        viewHolder.viewBinding {
            tvText.text = item
        }
    }
}