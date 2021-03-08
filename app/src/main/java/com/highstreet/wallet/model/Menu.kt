package com.highstreet.wallet.model

import android.app.Activity

/**
 * @author Yang Shihao
 * @Date 2020/10/20
 */
data class Menu(
    val title: String = "",
    val desc: String? = null,
    val icon: Any? = null,
    val action: Class<out Activity>? = null,
    val data: String? = null,
    val type: Int = TYPE_NORMAL
) {

    companion object {
        const val TYPE_NORMAL = 0
        const val TYPE_WIDE_LINE = 1
        const val TYPE_NARROW_LINE = 2
        const val TYPE_GROUP = 3
        const val TYPE_SPACE = 4

        fun wide() = Menu(type = TYPE_WIDE_LINE)
        fun narrow() = Menu(type = TYPE_NARROW_LINE)
        fun space() = Menu(type = TYPE_SPACE)
        fun group(name: String) = Menu(title = name, type = TYPE_GROUP)
    }
}