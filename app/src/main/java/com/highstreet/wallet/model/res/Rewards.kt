package com.highstreet.wallet.model.res

import com.highstreet.wallet.model.req.Coin
import com.highstreet.wallet.utils.StringUtils
import java.io.Serializable

/**
 * @author Yang Shihao
 * @Date 2020/10/27
 */
data class Rewards(
    val rewards: List<Reward>?,
    val total: List<Coin>?
) {
    fun getTotalReward(): String {
        val temp = if (total == null || total.isEmpty()) {
            null
        } else {
            total[0]
        }
        return StringUtils.pdip2DIP(temp, false)
    }
}

data class Reward(
    val reward: List<Coin>?,
    val validator_address: String?
) : Serializable {
    fun getReward(): String {
        val temp = if (reward == null || reward.isEmpty()) {
            null
        } else {
            reward[0]
        }
        return StringUtils.pdip2DIP(temp, false)
    }
}