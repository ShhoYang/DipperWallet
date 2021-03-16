package com.highstreet.wallet.utils

import android.text.TextUtils
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.cache.CoinPriceCache
import com.highstreet.wallet.constant.Constant
import com.highstreet.wallet.constant.Currency
import com.highstreet.wallet.model.req.Coin
import com.highstreet.wallet.model.req.Fee
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * @author Yang Shihao
 * @Date 2020/10/30
 */

object AmountUtils {

    private const val PDIP = "pdip"

    /**
     * 费用
     */
    fun generateFee(): Fee {
        val coin =
            Coin(BigDecimal(Constant.GAS_PRICE).multiply(BigDecimal(Constant.GAS)).toString(), PDIP)
        return Fee("${Constant.GAS}", arrayListOf(coin))
    }

    /**
     * @param amount DIP
     */
    fun generateCoin(amount: String): Coin {
        return Coin(
            BigDecimal(amount).multiply(BigDecimal(Constant.DIP_RATE)).toLong().toString(),
            PDIP
        )
    }

    /**
     * @param amount pdip
     */
    fun generateCoin(amount: Long, subtractFee: Boolean): Coin {
        val a = if (subtractFee) {
            BigDecimal(amount).subtract(BigDecimal(Constant.GAS_PRICE).multiply(BigDecimal(Constant.GAS)))
                .toLong().toString()
        } else {
            amount.toString()
        }
        return Coin(a, PDIP)
    }

    /**
     * 余额是否足够
     * @param balance pdip
     * @param amount DIP
     */
    fun isEnough(balance: String, amount: String): Boolean {
        return BigDecimal(balance).compareTo(BigDecimal(amount).multiply(BigDecimal(Constant.DIP_RATE))) > -1
    }

    fun getAmountValue(amount: String, currencyType: String? = null): String {
        val type = currencyType ?: AccountManager.instance().currencyType
        val price = CoinPriceCache.instance().getPrice(type)
        if (TextUtils.isEmpty(price)) {
            return amount
        }
        val currency = Currency.getCurrencyByType(type)
        return currency.symbol + BigDecimal(amount).multiply(BigDecimal(price))
            .setScale(currency.decimalPlaces, RoundingMode.DOWN).toPlainString()
    }

    fun getPrice(): String {
        val type = AccountManager.instance().currencyType
        val currency = Currency.getCurrencyByType(type)
        val price = CoinPriceCache.instance().getPrice(type)
        return currency.symbol + BigDecimal(price).setScale(
            currency.decimalPlaces,
            RoundingMode.DOWN
        ).toPlainString()
    }
}