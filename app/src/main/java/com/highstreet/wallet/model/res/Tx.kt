package com.highstreet.wallet.model.res

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hao.library.adapter.PagedAdapterItem
import com.highstreet.wallet.constant.Constant
import com.highstreet.wallet.constant.MsgType
import com.highstreet.wallet.model.req.Coin
import com.highstreet.wallet.model.req.StdTx
import com.highstreet.wallet.utils.StringUtils
import java.io.Serializable
import java.lang.Exception
import java.math.BigDecimal
import kotlin.collections.ArrayList

/**
 * @author Yang Shihao
 * @Date 2020/10/26
 */
data class Tx(
    val gas_used: String?,
    val gas_wanted: String?,
    val height: Long,
    val logs: List<Log>?,
    val timestamp: String?,
    val txhash: String?,
    val tx: StdTx?
) : PagedAdapterItem, Serializable {

    fun success(): Boolean {
        return if (logs == null || logs.isEmpty()) {
            false
        } else {
            true == logs[0].success
        }
    }

    fun getType(): String {
        val msgs = tx?.value?.msg
        return if (msgs == null || msgs.isEmpty()) {
            ""
        } else {
            msgs[0].type.replace("dip/Msg", "")
        }
    }

    /**
     * 费用
     */
    fun getFee(): String {
        if (gas_used == null || !TextUtils.isDigitsOnly(gas_used)) {
            return ""
        }

        val gas = tx?.value?.fee?.gas
        if (TextUtils.isEmpty(gas)) {
            return ""
        }

        val wantedFee = tx?.value?.fee?.amount
        val feeAmount = if (wantedFee == null || wantedFee.isEmpty()) {
            null
        } else {
            wantedFee[0]
        }

        if (feeAmount == null || TextUtils.isEmpty(feeAmount.amount)) {
            return ""
        }

        var r = BigDecimal(feeAmount.amount).divide(BigDecimal(gas)).multiply(BigDecimal(gas_used))
        return StringUtils.pdip2DIP(r.toString() + feeAmount.denom, false)
    }

    /**
     * 金额
     */
    fun getAmount(): String {
        val msgs = tx?.value?.msg
        val msg = if (msgs == null || msgs.isEmpty()) {
            null
        } else {
            msgs[0].value
        }

        var coins = if (msg?.amount == null) {
            null
        } else {
            try {
                Gson().fromJson<ArrayList<Coin>>(
                    Gson().toJson(
                        msg.amount
                            ?: "[]"
                    ), object : TypeToken<List<Coin>>() {}.type
                )
            } catch (e: Exception) {
                val coin = Gson().fromJson<Coin>(
                    Gson().toJson(
                        msg.amount
                            ?: "{}"
                    ), object : TypeToken<Coin>() {}.type
                )
                arrayListOf(coin)
            }
        }
        val coin = if (coins == null || coins.isEmpty()) {
            null
        } else {
            coins[0]
        }

        return StringUtils.pdip2DIP(coin)
    }

    /**
     * 转入地址
     */
    fun getFromAddress(): String {
        val msgs = tx?.value?.msg
        val msg = (if (msgs == null || msgs.isEmpty()) {
            null
        } else {
            msgs[0]
        }) ?: return ""

        val msgValue = msg.value

        val ret = when (msg.type) {
            MsgType.SEND -> msgValue.from_address
            MsgType.REDELEGATE -> msgValue.validator_src_address
            MsgType.UNDELEGATE -> msgValue.validator_address
            MsgType.RECEIVE_REWARD -> msgValue.validator_address
            else -> ""
        }

        return ret ?: ""
    }

    /**
     * 转出地址
     */
    fun getToAddress(): String {
        val msgs = tx?.value?.msg
        val msg = if (msgs == null || msgs.isEmpty()) {
            null
        } else {
            msgs[0].value
        }
        return msg?.to_address ?: ""
    }

    /**
     * 验证人地址
     */
    fun getValidatorAddress(): String {
        val msgs = tx?.value?.msg
        val msg = if (msgs == null || msgs.isEmpty()) {
            null
        } else {
            msgs[0].value
        }
        return msg?.validator_address ?: msg?.validator_dst_address ?: ""
    }

    fun getTime(): String {
        return StringUtils.utc2String(timestamp)
    }

    override fun getKey(): Any {
        return "$txhash"
    }
}

data class Log(
    val success: Boolean?
) : Serializable