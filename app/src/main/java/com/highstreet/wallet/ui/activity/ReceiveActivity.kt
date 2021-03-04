package com.highstreet.wallet.ui.activity

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.hao.library.annotation.AndroidEntryPoint
import com.hao.library.ui.BaseActivity
import com.hao.library.utils.DisplayUtils
import com.hao.library.viewmodel.PlaceholderViewModel
import com.highstreet.wallet.R
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.databinding.ActivityReceiveBinding
import com.highstreet.wallet.extensions.copy
import com.highstreet.wallet.view.listener.RxView

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */
@AndroidEntryPoint(injectViewModel = false)
class ReceiveActivity : BaseActivity<ActivityReceiveBinding, PlaceholderViewModel>() {

    override fun initView() {
        setTitle(R.string.QRCode)
        viewBinding {
            tvChainName.text = AccountManager.instance().account?.getUpperCaseChainName()
            val address = AccountManager.instance().address
            tvAddress.text = address

            RxView.click(btnCopy) {
                address.copy(this@ReceiveActivity)
            }
            val width = DisplayUtils.getScreenWidth(this@ReceiveActivity) / 2
            ivQr.setImageBitmap(generateQRCode(address, width, width))
        }
    }

    private fun generateQRCode(content: String, width: Int, height: Int): Bitmap? {
        val qrCodeWriter = QRCodeWriter()
        val hints: MutableMap<EncodeHintType, String?> = HashMap()
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
        try {
            val encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints)
            val pixels = IntArray(width * height)
            for (i in 0 until height) {
                for (j in 0 until width) {
                    if (encode[j, i]) {
                        pixels[i * width + j] = 0x000000
                    } else {
                        pixels[i * width + j] = 0XFFFFFF
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }

    override fun initData() {
    }
}