package com.highstreet.wallet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import com.hao.library.adapter.BaseMultiTypeNormalAdapter
import com.hao.library.adapter.ItemViewDelegate
import com.hao.library.adapter.ViewHolder
import com.hao.library.extensions.gone
import com.hao.library.extensions.visible
import com.highstreet.wallet.databinding.ItemWalletManageRightAddBinding
import com.highstreet.wallet.databinding.ItemWalletManageRightBinding
import com.highstreet.wallet.db.Account

/**
 * @author Yang Shihao
 * @Date 2020/10/22
 */

class WalletManageRightAdapter : BaseMultiTypeNormalAdapter<Account>() {

    var edit = false

    var itemTouchHelper: ItemTouchHelper? = null

    init {
        addDelegate(AccountItem())
        addDelegate(Add())
    }

    private inner class AccountItem : ItemViewDelegate<ItemWalletManageRightBinding, Account> {

        override fun isViewType(item: Account, position: Int) = item.address != ""

        override fun getViewBinding(
            layoutInflater: LayoutInflater,
            parent: ViewGroup
        ) = ItemWalletManageRightBinding.inflate(layoutInflater, parent, false)

        override fun bindViewHolder(
            viewHolder: ViewHolder<ItemWalletManageRightBinding>,
            item: Account,
            position: Int,
            payloads: MutableList<Any>
        ) {
            viewHolder.viewBinding {
                tvWalletName.text = item.nickName
                tvWalletAddress.text = item.address
                tvBalance.text = item.balance
                if (edit) {
                    ivSort.visible()
                    ivArrow.gone()
                    root.setOnClickListener(null)
                    root.setOnLongClickListener {
                        itemTouchHelper?.startDrag(viewHolder)
                        true
                    }
                } else {
                    ivArrow.visible()
                    ivSort.gone()
                    root.setOnLongClickListener(null)
                    root.setOnClickListener {
                        itemClickListener?.itemClicked(it, item, position)
                    }
                }
            }
        }
    }

    private inner class Add : ItemViewDelegate<ItemWalletManageRightAddBinding, Account> {

        override fun isViewType(item: Account, position: Int) = item.address == ""

        override fun getViewBinding(
            layoutInflater: LayoutInflater,
            parent: ViewGroup
        ) = ItemWalletManageRightAddBinding.inflate(layoutInflater, parent, false)

        override fun bindViewHolder(
            viewHolder: ViewHolder<ItemWalletManageRightAddBinding>,
            item: Account,
            position: Int,
            payloads: MutableList<Any>
        ) {
            viewHolder.viewBinding {
                root.setOnClickListener {
                    itemClickListener?.itemClicked(it, item, position)
                }
            }
        }
    }
}