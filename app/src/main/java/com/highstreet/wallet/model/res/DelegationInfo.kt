package com.highstreet.wallet.model.res

import com.hao.library.adapter.PagedAdapterItem
import com.highstreet.wallet.model.req.Coin
import java.io.Serializable

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

data class DelegationInfo(
        val balance: Coin?,
        val delegator_address: String,
        val entries: ArrayList<Entry>?,
        val shares: String?,
        val validator_address: String
) : PagedAdapterItem, Serializable {

    var completionTime = ""

    override fun getKey(): Any {
        return delegator_address + validator_address + shares
    }
}

data class Entry(
        val balance: String,
        val completion_time: String,
        val creation_height: String,
        val initial_balance: String
) : Serializable
