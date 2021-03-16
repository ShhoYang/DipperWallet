package com.highstreet.wallet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hao.library.adapter.BaseNormalAdapter
import com.hao.library.adapter.ViewHolder
import com.hao.library.extensions.visibility
import com.highstreet.wallet.databinding.DialogCenterMenuItemBinding

/**
 * @author Yang Shihao
 * @Date 3/6/21
 */
class CenterMenuDialogAdapter : BaseNormalAdapter<DialogCenterMenuItemBinding, String>() {

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ) = DialogCenterMenuItemBinding.inflate(layoutInflater, parent, false)

    override fun bindViewHolder(
        viewHolder: ViewHolder<DialogCenterMenuItemBinding>,
        item: String,
        position: Int,
        payloads: MutableList<Any>
    ) {
        viewHolder.viewBinding {
            tvText.text = item
            line.visibility(position != 0)
        }
    }
}