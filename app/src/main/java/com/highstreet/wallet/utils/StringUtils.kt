package com.highstreet.wallet.utils

import android.content.Context
import com.highstreet.wallet.R
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

    private val df = DecimalFormat("0.00%")


    fun formatPercent(s: String?): String {
        if (s == null || s == "" || s == "0") {
            return "0.00%"
        }
        return df.format(s.toDouble())
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
        val text = if (s.indexOf(".") > -1) {
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