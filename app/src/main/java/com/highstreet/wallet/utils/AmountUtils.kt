package com.highstreet.wallet.utils

import android.text.TextUtils
import com.hao.library.http.CustomException
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.R
import com.highstreet.wallet.cache.CoinPriceCache
import com.highstreet.wallet.constant.Constant
import com.highstreet.wallet.constant.Currency
import com.highstreet.wallet.model.req.Coin
import com.highstreet.wallet.model.req.Fee
import com.highstreet.wallet.model.res.AccountInfo
import com.highstreet.wallet.model.res.DelegationInfo
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * @author Yang Shihao
 * @Date 2020/10/30
 */

object AmountUtils {

    private const val PDIP = "pdip"
    private const val DIP = "DIP"
    private const val DIP_RATE = 1_000_000_000_000

    const val ZERO = "0.000000"
    const val UNIT_ZERO = "0.000000DIP"
    private val df = DecimalFormat("0.000000")

    @Throws(CustomException::class)
    fun checkAmount(accountInfo: AccountInfo?, toAmount: String, isAll: Boolean = false): Coin {
        if (accountInfo == null) {
            throw   CustomException(R.string.failed)
        }
        val coins = accountInfo.value?.coins
        val balance = if (coins == null || coins.isEmpty()) {
            null
        } else {
            coins[0].amount
        }
        return checkAmount(balance, toAmount, isAll)
    }

    @Throws(CustomException::class)
    fun checkAmount(balance: String?, toAmount: String, isAll: Boolean = false): Coin {
        if (balance == null) {
            throw   CustomException(R.string.failed)
        }
        var amount = when {
            isAll -> Coin.generateCoin(balance, true)
            isEnough(balance, toAmount) -> Coin.generateCoin(toAmount)
            else -> null
        }

        if (amount == null) {
            throw  CustomException(R.string.notEnough)
        }

        return amount
    }

    @Throws(CustomException::class)
    fun checkAmount2(balance: String?, toAmount: String, isAll: Boolean = false): String {
        if (balance == null) {
            throw   CustomException(R.string.failed)
        }
        var amount = when {
            isAll -> balance
            isEnough(balance, toAmount) -> toAmount
            else -> null
        }

        if (amount == null) {
            throw  CustomException(R.string.notEnough)
        }

        return amount
    }

    /**
     * 费用
     */
    fun generateFee(): Fee {
        val coin =
            Coin(
                BigDecimal(Constant.GAS_PRICE).multiply(BigDecimal(Constant.GAS)).toPlainString(),
                PDIP
            )
        return Fee("${Constant.GAS}", arrayListOf(coin))
    }

    /**
     * 余额是否足够
     * @param balance pdip
     * @param amount DIP
     */
    fun isEnough(balance: String, amount: String): Boolean {
        return BigDecimal(balance).compareTo(BigDecimal(amount).multiply(BigDecimal(Constant.DIP_RATE))) > -1
    }

    /**
     * token
     * 单位相等
     */
    fun isEnough2(balance: String, amount: String): Boolean {
        return BigDecimal(balance).compareTo(BigDecimal(amount)) > -1
    }

    fun getAmountValue(amount: String?, currencyType: String? = null): String {
        var amount1 = amount
        val type = currencyType ?: AccountManager.instance().currencyType
        var price = CoinPriceCache.instance().getPrice(type)
        val currency = Currency.getCurrencyByType(type)

        if (TextUtils.isEmpty(amount1) || "--" == amount1) {
            amount1 = "0"
        }

        if (TextUtils.isEmpty(price)) {
            price = "0"
        }

        return currency.symbol + BigDecimal(amount1).multiply(BigDecimal(price))
            .setScale(currency.decimalPlaces, RoundingMode.DOWN).toPlainString()
    }

    fun getPrice(): String {
        val type = AccountManager.instance().currencyType
        val currency = Currency.getCurrencyByType(type)
        var price = CoinPriceCache.instance().getPrice(type)

        if (TextUtils.isEmpty(price)) {
            price = "0"
        }

        return currency.symbol + BigDecimal(price).setScale(
            currency.decimalPlaces,
            RoundingMode.DOWN
        ).toPlainString()
    }

    fun formatDecimal(s: String?): String {
        if (s == null || s.isEmpty()) {
            return ""
        }

        if (s.contains(".")) {
            return s.substring(0, s.indexOf("."))
        }
        return s
    }

    fun pdip2DIP(coin: Coin?, unit: Boolean = true): String {
        if (coin == null) {
            return formatUnit(ZERO, unit)
        }

        return pdip2DIP("${coin.amount}${coin.denom}", unit)
    }

    fun pdip2DIP(amount: String?, unit: Boolean = false): String {
        if (amount == null || amount.isEmpty()) {
            return formatUnit(ZERO, unit)
        }

        val temp = if (amount.endsWith(PDIP)) {
            amount.replace(PDIP, "")
        } else {
            amount
        }

        if (temp == "" || temp == "0") {
            return formatUnit(ZERO, unit)
        }

        if (amount.endsWith(DIP)) {
            return formatUnit(df.format(amount.replace(DIP, "")), unit)
        }

        try {
            val bigDecimal = BigDecimal(temp)
            return bigDecimal.divide(BigDecimal(DIP_RATE)).setScale(6, RoundingMode.DOWN)
                .toPlainString()
        } catch (e: Exception) {

        }
        return amount
    }

    private fun formatUnit(value: String, unit: Boolean): String {
        return if (unit) {
            value + DIP
        } else {
            value
        }
    }

    fun processDelegations(list: ArrayList<DelegationInfo>?): String {
        if (list == null || list.isEmpty()) {
            return ZERO
        }
        var amount = 0L

        list.forEach {
            amount += it.shares?.toDouble()?.toLong() ?: 0
        }
        return pdip2DIP(amount.toString())
    }

    fun processUnbondingDelegations(list: ArrayList<DelegationInfo>?): String {
        if (list == null || list.isEmpty()) {
            return ZERO
        }
        var amount = 0L

        list.forEach {
            it.entries?.forEach { e ->
                amount += e.balance.toDouble().toLong()
            }
        }
        return pdip2DIP(amount.toString())
    }

    fun getTokenAmount(balance: String?, decimals: String?): String {
        if (TextUtils.isEmpty(balance)
            || TextUtils.isEmpty(decimals)
            || !TextUtils.isDigitsOnly(decimals)
        ) {
            return ZERO
        }

        val d = decimals!!.toInt()

        if (d <= 0) {
            return ZERO
        }

        return BigDecimal(balance).divide(BigDecimal(10).pow(d)).setScale(6, RoundingMode.DOWN)
            .toPlainString()
    }
}