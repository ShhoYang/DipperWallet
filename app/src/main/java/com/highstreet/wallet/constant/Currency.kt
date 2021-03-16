package com.highstreet.wallet.constant

/**
 * @author Yang Shihao
 * @Date 3/16/21
 */
enum class Currency(val type: String, val symbol: String, val decimalPlaces: Int) {
    USD("USD", "$", 2),
    EUR("EUR", "€", 2),
    KRW("KRW", "₩", 2),
    JPY("JPY", "¥", 2),
    CNY("CNY", "¥", 2),
    BTC("BTC", "\u20BF", 8);

    companion object {
        fun getSymbolByType(type: String): String {
            val values = values()
            values.forEach {
                if (it.type.equals(type, true)) {
                    return it.symbol
                }
            }

            return "¥"
        }

        fun getCurrencyByType(type: String): Currency {
            val values = values()
            values.forEach {
                if (it.type.equals(type, true)) {
                    return it
                }
            }

            return CNY
        }
    }
}