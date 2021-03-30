package com.highstreet.wallet.model.req

import com.highstreet.wallet.constant.Constant
import java.io.Serializable
import java.math.BigDecimal

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */
data class Coin(
    val amount: String?,
    val denom: String?
) : Serializable {
    companion object {

        private const val PDIP = "pdip"

        val ZERO = Coin("0", PDIP)

        /**
         * @param amount DIP
         */
        fun generateCoin(amount: String): Coin {
            return Coin(
                BigDecimal(amount).multiply(BigDecimal(Constant.DIP_RATE)).toLong().toString(), PDIP
            )
        }

        /**
         * @param amount pdip
         */
        fun generateCoin(amount: String, subtractFee: Boolean): Coin {
            val a = if (subtractFee) {
                BigDecimal(amount)
                    .subtract(
                        BigDecimal(Constant.GAS_PRICE).multiply(BigDecimal(Constant.GAS))
                    ).toLong()
                    .toString()
            } else {
                amount.toString()
            }
            return Coin(a, PDIP)
        }
    }
}