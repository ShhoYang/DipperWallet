package com.highstreet.wallet.model.req

import java.io.Serializable

/**
 * @author Yang Shihao
 * @Date 1/20/21
 */
data class Fee(
    val gas: String,
    val amount: ArrayList<Coin>
) : Serializable