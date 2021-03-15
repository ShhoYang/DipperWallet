package com.highstreet.wallet.model.res

import android.text.TextUtils
import com.hao.library.adapter.PagedAdapterItem
import com.highstreet.wallet.utils.StringUtils
import java.io.Serializable
import java.text.DecimalFormat

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

data class Validator(
    val commission: Commission?,
    val consensus_pubkey: String?,
    val delegator_shares: String?,
    val description: Description?,
    val jailed: Boolean?,
    val min_self_delegation: String?,
    val operator_address: String?,
    val self_delegation: String?,
    val status: Int?,
    val tokens: String?,
    val unbonding_height: String?,
    val unbonding_time: String?,
    // 附加
    var delegationInfo: DelegationInfo?,
    var unbondingDelegationInfo: DelegationInfo?,
    var reward: Reward?
) : PagedAdapterItem, Serializable {
    override fun getKey(): Any {
        return operator_address + consensus_pubkey
    }

    private var firstLetterName: String? = null

    fun getFirstLetterName(): String {

        if (null == firstLetterName) {
            val name = description?.moniker
            if (name == null || name.isEmpty()) {
                firstLetterName = ""
            } else {
                val l = name[0]

                firstLetterName = if (l in 'a'..'z' || l in 'A'..'Z') {
                    l.toString().toUpperCase()
                } else {
                    l.toString()
                }
            }
        }

        return firstLetterName!!
    }

    fun getDelegatorShares(): String {
        return StringUtils.pdip2DIP(delegator_shares)
    }

    fun getSelfDelegation(): String {
        return StringUtils.pdip2DIP(self_delegation)
    }

    fun getRate(): String {
        return StringUtils.formatPercent(commission?.commission_rates?.rate)
    }

    fun getProfile(): String {
        val sb = StringBuilder()
        description?.apply {
            if (!TextUtils.isEmpty(identity)) {
                sb.append(identity).append("\n")
            }
            if (!TextUtils.isEmpty(website)) {
                sb.append(website).append("\n")
            }
            if (!TextUtils.isEmpty(details)) {
                sb.append(details)
            }
        }
        return sb.toString()
    }

}

data class Commission(
    val commission_rates: CommissionRates?,
    val update_time: String?
) : Serializable

data class Description(
    val details: String?,
    val identity: String?,
    val moniker: String?,
    val website: String?
) : Serializable

data class CommissionRates(
    val max_change_rate: String?,
    val max_rate: String?,
    val rate: String?
) : Serializable