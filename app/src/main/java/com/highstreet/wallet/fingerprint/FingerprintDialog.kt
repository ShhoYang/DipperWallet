package com.highstreet.wallet.fingerprint

import android.app.Activity
import android.graphics.Color
import androidx.core.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.hao.library.extensions.gone
import com.hao.library.extensions.visible
import com.hao.library.utils.DisplayUtils
import com.hao.library.utils.DrawableUtils
import com.hao.library.view.dialog.BaseDialog
import com.highstreet.wallet.R
import com.highstreet.wallet.databinding.DialogFingerprintBinding
import com.highstreet.wallet.extensions.string
import com.highstreet.wallet.fingerprint.listener.FingerprintDialogListener

/**
 * @author Yang Shihao
 * @Date 2020/10/20
 */
class FingerprintDialog(activity: Activity) :
    BaseDialog<DialogFingerprintBinding>(activity = activity),
    View.OnClickListener {

    private val colorFailed = 0xFFDC143C.toInt()
    private val colorSucceed = 0xFF3B64DB.toInt()
    private val colorTip = 0xFF353535.toInt()

    private var dialogParams: DialogParams =
        DialogParams(useFingerprint = true, showUserPassword = true)

    private var fingerprintDialogListener: FingerprintDialogListener? = null

    override fun getVB() = DialogFingerprintBinding.inflate(layoutInflater)

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
            etPassword.background = drawable
            tvCancel.setOnClickListener(this@FingerprintDialog)
            tvCancel2.setOnClickListener(this@FingerprintDialog)
            tvUsePassword.setOnClickListener(this@FingerprintDialog)
            tvConfirm.setOnClickListener(this@FingerprintDialog)
            setOnCancelListener {
                fingerprintDialogListener?.cancelFingerprint()
            }
        }
    }

    override fun show() {
        reset()
        super.show()
    }

    private fun reset() {
        viewBinding {
            tvMsg.setText(R.string.verifyFingerprint)
            tvMsg.setTextColor(colorTip)
            ivFingerprint.setImageResource(R.mipmap.fingerprint)
            etPassword.setText("")
            etPassword.error = null
            if (dialogParams.useFingerprint) {
                llFingerprint.visible()
                llPassword.gone()
            } else {
                llFingerprint.gone()
                llPassword.visible()
            }
        }
    }

    fun setTip(msg: String, isSucceed: Boolean) {
        viewBinding {
            tvMsg.text = msg
            if (isSucceed) {
                tvMsg.setTextColor(colorSucceed)
                ivFingerprint.setImageResource(R.mipmap.fingerprint_1)
            } else {
                tvMsg.setTextColor(colorFailed)
                ivFingerprint.setImageResource(R.mipmap.fingerprint)
            }
        }
    }

    fun setDialogParams(dialogParams: DialogParams): FingerprintDialog {
        this.dialogParams = dialogParams
        viewBinding {
            if (dialogParams.showUserPassword) {
                lineV.visible()
                tvUsePassword.visible()
            } else {
                lineV.gone()
                tvUsePassword.gone()
            }
        }
        return this
    }

    fun setListener(fingerprintDialogListener: FingerprintDialogListener?): FingerprintDialog {
        this.fingerprintDialogListener = fingerprintDialogListener
        return this
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvCancel,  R.id.tvCancel2 -> {
                fingerprintDialogListener?.cancelFingerprint()
                dismiss()
            }

            R.id.tvUsePassword -> {
                fingerprintDialogListener?.cancelFingerprint()
                viewBinding {
                    llFingerprint.gone()
                    llPassword.visible()
                }
            }

            R.id.tvConfirm -> {
                viewBinding {
                    if (true == fingerprintDialogListener?.usePassword(etPassword.string())) {
                        dismiss()
                    } else {
                        etPassword.error = context.getString(R.string.wrongPassword)
                    }
                }
            }
        }
    }
}