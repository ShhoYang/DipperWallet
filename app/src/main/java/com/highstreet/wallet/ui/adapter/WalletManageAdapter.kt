package com.highstreet.wallet.ui.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.highstreet.lib.adapter.BaseNormalAdapter
import com.highstreet.lib.adapter.ViewHolder
import com.highstreet.lib.utils.DisplayUtils
import com.highstreet.lib.utils.DrawableUtils
import com.highstreet.wallet.R
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.db.Account

/**
 * @author Yang Shihao
 * @Date 2020/10/22
 */

class WalletManageAdapter() : BaseNormalAdapter<Account>(R.layout.g_item_wallet_manage) {

    private lateinit var normalDrawable: Drawable
    private lateinit var selectedDrawable: Drawable
    private lateinit var backupDrawable: Drawable

    constructor(context: Context) : this() {
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

    override fun bindViewHolder(holder: ViewHolder, item: Account, position: Int) {
        holder.getView<ConstraintLayout>(R.id.clContent).background = if (item.isLast) {
            holder.visible(R.id.ivSelected)
            selectedDrawable
        } else {
            holder.gone(R.id.ivSelected)
            normalDrawable
        }

        holder.getView<TextView>(R.id.tvBackup).background = backupDrawable

        holder.setText(R.id.tvWalletName, item.nickName)
            .setText(R.id.tvWalletAddress, item.address)
            .setText(R.id.tvAmount, item.address)

        holder.setClickListener(
            arrayOf(
                R.id.tvWalletAddress,
                R.id.ivCopy,
                R.id.tvBackup,
                R.id.ivEdit,
                R.id.ivDelete
            )
        ) {
            itemClickListener?.itemClicked(it, item, position)
        }
    }
}