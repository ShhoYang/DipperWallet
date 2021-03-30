package com.highstreet.wallet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hao.library.adapter.BaseNormalAdapter
import com.hao.library.adapter.ViewHolder
import com.hao.library.extensions.load
import com.hao.library.extensions.visibility
import com.highstreet.wallet.databinding.DialogCenterMenuItem2Binding
import com.highstreet.wallet.view.IOptionItem

/**
 * @author Yang Shihao
 * @Date 2020/10/22
 */

class CenterMenuDialogAdapter2<T : IOptionItem> :
    BaseNormalAdapter<DialogCenterMenuItem2Binding, T>() {

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ) = DialogCenterMenuItem2Binding.inflate(layoutInflater, parent, false)

    override fun bindViewHolder(
        viewHolder: ViewHolder<DialogCenterMenuItem2Binding>,
        item: T,
        position: Int,
        payloads: MutableList<Any>
    ) {
        viewHolder.viewBinding {
            line.visibility(position != 0)
            ivChainIcon.load(item.getShowIcon())
            tvChainName.text = item.getShowText()
        }
    }
}