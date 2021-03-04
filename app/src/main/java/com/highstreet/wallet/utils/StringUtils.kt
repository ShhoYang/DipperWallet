package com.highstreet.wallet.utils

import android.content.Context
import android.text.TextUtils
import com.highstreet.wallet.R
import com.highstreet.wallet.model.req.Coin
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

object StringUtils {

    private const val SECOND = 1000
    private const val MINUTE = SECOND * 60
    private const val HOUR = MINUTE * 60
    private const val DAY = HOUR * 24

    private const val PDIP = "pdip"
    private const val DIP = "DIP"
    private const val DIP_RATE = 1_000_000_000_000

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
            return "0DIP"
        }

        return pdip2DIP("${coin.amount}${coin.denom}", unit)
    }

    fun pdip2DIP(amount: String?, unit: Boolean = true): String {

        if (amount == null || amount.isEmpty()) {
            return formatUnit("0", unit)
        }

        if (amount.endsWith(DIP)) {
            return amount
        }

        val temp = if (amount.endsWith(PDIP)) {
            amount.replace(PDIP, "")
        } else {
            amount
        }

        if (temp == "" || temp == "0") {
            return formatUnit("0", unit)
        }

        val df = DecimalFormat("#.######")
        if (TextUtils.isDigitsOnly(temp)) {
            val l = temp.toLong().toDouble() / DIP_RATE
            return formatUnit(df.format(l), unit)
        } else if (temp.contains(".")) {
            val l = temp.toDouble() / DIP_RATE
            return formatUnit(df.format(l), unit)
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

    fun utc2String(s: String?): String {
        val date = utc2Date(s) ?: return ""
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            sdf.format(date)
        } catch (e: Exception) {
            ""
        }
    }

    fun timeGap(context: Context, s: String?): String {

        val temp = utc2Date(s)?.time ?: return ""

        val dayS: String = context.getString(R.string.day)
        val hourS: String = context.getString(R.string.hour)
        val minuteS: String = context.getString(R.string.minute)
        val secondS: String = context.getString(R.string.second)

        val cur = System.currentTimeMillis()
        val gap = temp - cur
        if (gap <= SECOND) {
            return "0$secondS"
        }

        val day = gap / DAY
        val hour = (gap % DAY) / HOUR
        val minute = (gap % HOUR) / MINUTE
        val second = (gap % MINUTE) / SECOND

        val sb = StringBuffer()

        if (gap > day) {
            sb.append(day).append(dayS)
        }

        if (gap > hour) {
            sb.append(hour).append(hourS)
        }

        if (gap > minute) {
            sb.append(minute).append(minuteS)
        }

        sb.append(second).append(secondS)

        return sb.toString()
    }

    private fun utc2Date(s: String?): Date? {
        if (s == null || s.isEmpty()) {
            return null
        }
        var text = if (s.indexOf(".") > -1) {
            s.substring(0, s.indexOf(".")) + "Z"
        } else {
            s
        }

        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            sdf.parse(text)
        } catch (e: Exception) {
            null
        }
    }
}