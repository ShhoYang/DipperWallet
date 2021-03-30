package com.highstreet.wallet.model

import com.highstreet.wallet.constant.Chain
import com.highstreet.wallet.db.Account
import com.highstreet.wallet.view.IOptionItem

/**
 * @author Yang Shihao
 * @Date 3/12/21
 */
data class WalletType(
    val chain: Chain,
    val chainIcon: Int,
    var accounts: List<Account> = arrayListOf(),
    var selected: Boolean = false,
    val isAll: Boolean = false
) : IOptionItem {
    override fun getShowText(): String {
        return chain.showName
    }

    override fun getShowIcon(): Any {
        return chainIcon
    }
}