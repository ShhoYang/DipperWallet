package com.highstreet.wallet.model.res

import com.hao.library.adapter.PagedAdapterItem

/**
 * @author Yang Shihao
 * @Date 3/6/21
 */
data class Token(
    val icon: String,
    val name: String,
    val balance: String
) : PagedAdapterItem {
    override fun getKey(): Any {
        return name
    }
}
