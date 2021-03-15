package com.highstreet.wallet.utils

import android.view.View
import com.highstreet.wallet.constant.Colors

/**
 * @author Yang Shihao
 * @Date 3/10/21
 */
object ViewUtils {

    fun updateLineStyle(view: View, hasFocus: Boolean) {
        view.setBackgroundColor(if (hasFocus) Colors.editLineFocus else Colors.editLineBlur)
    }
}