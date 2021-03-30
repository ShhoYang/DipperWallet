package com.highstreet.wallet.model

/**
 * @author Yang Shihao
 * @Date 2020/10/20
 */
data class Menu(
    val icon: Int = 0,
    val title: String = "",
    val desc: String? = null,
    val action: Int = -1,
    val type: Int = TYPE_NORMAL
) {

    companion object {
        const val TYPE_NORMAL = 0
        const val TYPE_GROUP = 2
        const val TYPE_SPACE = 3

        fun space() = Menu(type = TYPE_SPACE)
        fun group(name: String) = Menu(title = name, type = TYPE_GROUP)
    }
}