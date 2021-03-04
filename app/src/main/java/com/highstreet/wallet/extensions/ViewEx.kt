package com.highstreet.wallet.extensions

import android.widget.EditText

/**
 * @author Yang Shihao
 * @Date 2/26/21
 */

fun EditText.string(): String {
    return text.toString().trim()
}