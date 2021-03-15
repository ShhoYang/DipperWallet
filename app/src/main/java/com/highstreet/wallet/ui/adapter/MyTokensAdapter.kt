package com.highstreet.wallet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hao.library.adapter.BaseNormalAdapter
import com.hao.library.adapter.BasePagedAdapter
import com.hao.library.adapter.ViewHolder
import com.highstreet.wallet.databinding.ItemMyTokenBinding
import com.highstreet.wallet.db.Account
import com.highstreet.wallet.model.res.Token

/**
 * @author Yang Shihao
 * @Date 3/6/21
 */
class MyTokensAdapter : BaseNormalAdapter<ItemMyTokenBinding, Account>() {

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemMyTokenBinding.inflate(layoutInflater, parent, false)

    override fun bindViewHolder(
        viewHolder: ViewHolder<ItemMyTokenBinding>,
        item: Account,
        position: Int,
        payloads: MutableList<Any>
    ) {
    }
}