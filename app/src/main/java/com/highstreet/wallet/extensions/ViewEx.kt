package com.highstreet.wallet.extensions

import android.view.View
import android.widget.EditText
import com.highstreet.wallet.constant.Colors

/**
 * @author Yang Shihao
 * @Date 2/26/21
 */

fun EditText.string(): String {
    return text.toString().trim()
}

fun EditText.focusListener(targetView: View) {
    setOnFocusChangeListener { _, hasFocus ->
        targetView.setBackgroundColor(if (hasFocus) Colors.editLineFocus else Colors.editLineBlur)
    }
}