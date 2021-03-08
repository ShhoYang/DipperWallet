package com.highstreet.wallet.utils

import android.content.Context
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.Constant
import com.highstreet.wallet.ui.activity.WebActivity

/**
 * @author Yang Shihao
 * @Date 3/6/21
 */
object JumpUtils {

    fun toDipperNetwork(context: Context) {
        WebActivity.start(
            context,
            context.getString(R.string.dipperNetwork),
            Constant.DIPPER_NETWORK
        )
    }
}