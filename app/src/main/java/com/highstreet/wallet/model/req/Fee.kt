package com.highstreet.wallet.model.req

/**
 * @author Yang Shihao
 * @Date 1/20/21
 */
data class Fee(
        val gas: String,
        val amount: ArrayList<Coin>
)