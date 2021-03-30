package com.highstreet.wallet.ui.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hao.library.adapter.BaseNormalAdapter
import com.hao.library.adapter.ViewHolder
import com.hao.library.extensions.load
import com.highstreet.wallet.R
import com.highstreet.wallet.databinding.ItemMyTokenBinding
import com.highstreet.wallet.db.Token

/**
 * @author Yang Shihao
 * @Date 3/6/21
 */
class MyTokensAdapter : BaseNormalAdapter<ItemMyTokenBinding, Token>() {

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemMyTokenBinding.inflate(layoutInflater, parent, false)

    override fun bindViewHolder(
        viewHolder: ViewHolder<ItemMyTokenBinding>,
        item: Token,
        position: Int,
        payloads: MutableList<Any>
    ) {
        viewHolder.viewBinding {
            val icon = item.icon
            if(TextUtils.isEmpty(icon)){
                ivIcon.setImageResource(R.mipmap.logo2)
            }else {
                ivIcon.load(icon)
            }
            tvName.text = item.name
            tvDesc.text = item.desc
            tvAmount.text = item.balance
            tvAmountValue.text = item.balanceAmount
        }
    }
}