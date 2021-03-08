package com.highstreet.wallet.view

import android.app.Activity
import android.graphics.Color
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.hao.library.utils.DisplayUtils
import com.hao.library.utils.DrawableUtils
import com.hao.library.view.dialog.BaseDialog
import com.highstreet.wallet.databinding.DialogQrBinding
import com.highstreet.wallet.extensions.copy
import com.highstreet.wallet.utils.QRUtils

/**
 * @author Yang Shihao
 * @Date 3/6/21
 */
class QRDialog(activity: Activity) : BaseDialog<DialogQrBinding>(activity = activity) {

    private var address = ""

    override fun getVB() = DialogQrBinding.inflate(layoutInflater)

    override fun setWindowParams(window: Window) {
        val attributes = window.attributes
        val w = DisplayUtils.getScreenWidth(activity)
        attributes.width = w / 10 * 8
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        attributes.gravity = Gravity.CENTER
        window.attributes = attributes
        window.setBackgroundDrawable(DrawableUtils.generateRoundRectDrawable(12.0F, Color.WHITE))
        setCancelable(true)
        setCanceledOnTouchOutside(true)
    }

    override fun initView() {
        viewBinding {
            tvCancel.setOnClickListener {
                dismiss()
            }
            tvCopy.setOnClickListener {
                address.copy(activity)
                dismiss()
            }
        }
    }

    fun show(walletName: String, address: String) {
        this.address = address
        val w = DisplayUtils.dp2px(activity, 200)
        viewBinding {
            tvName.text = walletName
            tvAddress.text = address
            ivQr.setImageBitmap(QRUtils.generateQRCode(address, w, w))
        }
        show()
    }
}