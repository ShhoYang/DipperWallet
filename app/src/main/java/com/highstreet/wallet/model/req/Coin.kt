package com.highstreet.wallet.model.req

import java.io.Serializable

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */
data class Coin(
        val amount: String?,
        val denom: String?
) : Serializable