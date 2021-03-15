package com.highstreet.wallet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hao.library.adapter.BaseNormalAdapter
import com.hao.library.adapter.ViewHolder
import com.hao.library.extensions.load
import com.highstreet.wallet.constant.Icons
import com.highstreet.wallet.databinding.ItemTokenBinding
import com.highstreet.wallet.model.res.Token

/**
 * @author Yang Shihao
 * @Date 3/10/21
 */
class TokenAdapter : BaseNormalAdapter<ItemTokenBinding, Token>() {

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
        viewHolder.viewBinding {
            ivIcon.load(Icons.get(position + 1))
            tvName.text = item.name
            tvBalance.text = item.balance
        }
    }
}