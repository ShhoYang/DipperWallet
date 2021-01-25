package com.highstreet.wallet.model.res

import com.highstreet.wallet.model.req.Coin

/**
 * @author Yang Shihao
 * @Date 2020/10/27
 */
data class Rewards(
        val rewards: List<Reward>?,
        val total: List<Coin>?
)

data class Reward(
        val reward: List<Coin>?,
        val validator_address: String?
)