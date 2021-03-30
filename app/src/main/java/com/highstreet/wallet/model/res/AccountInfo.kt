package com.highstreet.wallet.model.res

import android.text.TextUtils
import com.highstreet.wallet.model.req.Coin
import com.highstreet.wallet.model.req.PublicKey
import com.highstreet.wallet.utils.AmountUtils

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */
data class AccountInfo(
    val type: String?,
    val value: AccountValue?
) {
    fun getAccountNumber(): Int {
        val accountNumber = value?.account_number
        if (accountNumber != null && TextUtils.isDigitsOnly(accountNumber)) {
            return accountNumber.toInt()
        }
        return 0
    }

    fun getSequence(): Int {
        val sequence = value?.sequence
        if (sequence != null && TextUtils.isDigitsOnly(sequence)) {
            return sequence.toInt()
        }
        return 0
    }

    fun getAmount(): String {
        val coins = value?.coins
        val coin = if (null == coins || coins.isEmpty()) {
            null
        } else {
            coins[0]
        }
        return AmountUtils.pdip2DIP(coin, false)
    }

    fun getLongAmount(): Long {
        val coins = value?.coins
        if (null != coins && coins.isNotEmpty()) {
            val coinBean = coins[0]
            return coinBean.amount?.toLong() ?: 0L
        }
        return 0L
    }
}

data class AccountValue(
    val account_number: String?,
    val address: String?,
    val code_hash: String?,
    val coins: List<Coin>?,
    val public_key: PublicKey?,
    val sequence: String?
)

