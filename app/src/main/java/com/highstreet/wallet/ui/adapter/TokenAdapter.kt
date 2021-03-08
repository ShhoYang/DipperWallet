package com.highstreet.wallet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hao.library.adapter.BasePagedAdapter
import com.hao.library.adapter.ViewHolder
import com.highstreet.wallet.databinding.ItemTokenBinding
import com.highstreet.wallet.model.res.Token

/**
 * @author Yang Shihao
 * @Date 3/6/21
 */
class TokenAdapter : BasePagedAdapter<ItemTokenBinding, Token>() {

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemTokenBinding.inflate(layoutInflater, parent, false)

    override fun bindViewHolder(
        viewHolder: ViewHolder<ItemTokenBinding>,
        item: Token,
        position: Int,
        payloads: MutableList<Any>
    ) {
    }
}