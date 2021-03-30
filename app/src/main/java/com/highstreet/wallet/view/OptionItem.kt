package com.highstreet.wallet.view

/**
 * @author Yang Shihao
 * @Date 3/17/21
 */
data class OptionItem(
    val text: String,
    val icon: Any = 0
) : IOptionItem {
    override fun getShowText(): String {
        return text
    }

    override fun getShowIcon(): Any {
        return icon
    }
}