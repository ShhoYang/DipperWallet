package com.highstreet.wallet.ui.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.hao.library.adapter.BaseNormalAdapter
import com.hao.library.adapter.ViewHolder
import com.hao.library.extensions.gone
import com.hao.library.utils.DisplayUtils
import com.hao.library.utils.DrawableUtils
import com.highstreet.wallet.App
import com.highstreet.wallet.R
import com.highstreet.wallet.databinding.ItemWalletManageBinding
import com.highstreet.wallet.db.Account

/**
 * @author Yang Shihao
 * @Date 2020/10/22
 */

class WalletManageAdapter : BaseNormalAdapter<ItemWalletManageBinding, Account>() {

    private var normalDrawable: Drawable
    private var selectedDrawable: Drawable
    private var backupDrawable: Drawable

    init {
        val context = App.instance
        val width = DisplayUtils.dp2px(context, 1)
        val radius = DisplayUtils.dp2px(context, 6).toFloat()
        normalDrawable = DrawableUtils.generateRoundRectBorderDrawable(
            radius,
            width,
            ContextCompat.getColor(context, R.color.line)
        )
        selectedDrawable = DrawableUtils.generateRoundRectBorderDrawable(
            radius,
            width,
            ContextCompat.getColor(context, R.color.colorPrimary)
        )
        backupDrawable = DrawableUtils.generateRoundRectBorderDrawable(
            100.0F,
            width,
            ContextCompat.getColor(context, R.color.text_orange)
        )
    }

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ) = ItemWalletManageBinding.inflate(layoutInflater, parent, false)

    override fun bindViewHolder(
        viewHolder: ViewHolder<ItemWalletManageBinding>,
        item: Account,
        position: Int,
        payloads: MutableList<Any>
    ) {
        viewHolder.viewBinding {
            if (item.isLast) {
                clContent.background = selectedDrawable
                ivSelected.visibility
            } else {
                clContent.background = normalDrawable
                ivSelected.gone()
            }
            tvBackup.background = backupDrawable
            tvWalletName.text = item.nickName
            tvWalletAddress.text = item.address
            tvAmount.text = item.address

            val click: (View) -> Unit = {
                itemClickListener?.itemClicked(it, item, position)
            }

            tvWalletAddress.setOnClickListener(click)
            ivCopy.setOnClickListener(click)
            tvBackup.setOnClickListener(click)
            ivEdit.setOnClickListener(click)
            ivDelete.setOnClickListener(click)
        }
    }
}