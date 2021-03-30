package com.highstreet.wallet.model.req

/**
 * @author Yang Shihao
 * @Date 3/19/21
 */
data class EstimateGas(
    val from: String,
    val to: String,
    val payload: String,
    val amount: Coin
)