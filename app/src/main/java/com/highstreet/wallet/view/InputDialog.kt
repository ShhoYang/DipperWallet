package com.highstreet.wallet.view

import android.app.Activity
import android.graphics.Color
import androidx.core.content.ContextCompat
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.hao.library.utils.DisplayUtils
import com.hao.library.utils.DrawableUtils
import com.hao.library.view.dialog.BaseDialog
import com.highstreet.wallet.R
import com.highstreet.wallet.databinding.DialogInputBinding
import com.highstreet.wallet.extensions.string

/**
 * @author Yang Shihao
 * @Date 2020/10/21
 */

class InputDialog(activity: Activity) : BaseDialog<DialogInputBinding>(activity = activity) {

    private var inputDialogListener: InputDialogListener? = null

    override fun getVB() = DialogInputBinding.inflate(layoutInflater)

    override fun setWindowParams(window: Window) {
        val attributes = window.attributes
        val w = DisplayUtils.getScreenWidth(activity)
        attributes.width = w / 10 * 8
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        attributes.gravity = Gravity.CENTER
        window.attributes = attributes
        window.setBackgroundDrawable(DrawableUtils.generateRoundRectDrawable(12.0F, Color.WHITE))
        setCancelable(false)
    }

    override fun initView() {
        val drawable = DrawableUtils.generateRoundRectBorderDrawable(
            12.0F,
            DisplayUtils.dp2px(activity, 1),
            ContextCompat.getColor(activity, R.color.lightGray)
        )
        viewBinding {
            etContent.background = drawable
            tvCancel.setOnClickListener {
                dismiss()
            }
            tvConfirm.setOnClickListener {
                inputDialogListener?.confirm(etContent.string())
                dismiss()
            }
        }
    }

    fun setTitle(title: String): InputDialog {
        viewBinding {
            tvTitle.text = title
        }
        return this
    }

    fun setHint(hint: String): InputDialog {
        viewBinding {
            etContent.hint = hint
        }
        return this
    }

    fun setText(text: String): InputDialog {
        viewBinding {
            etContent.setText(text)
        }
        return this
    }

    fun setListener(inputDialogListener: InputDialogListener?): InputDialog {
        this.inputDialogListener = inputDialogListener
        return this
    }
}

interface InputDialogListener {
    fun confirm(content: String)
}