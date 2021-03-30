package com.highstreet.wallet.model.req

import java.io.Serializable


data class Msg(
    val type: String,
    val value: MsgValue
) : Serializable

class MsgValue : Serializable {
    var from_address: String? = null
    var to_address: String? = null
    var value: Coin? = null
    var amount: Any? = null
    var delegator_address: String? = null
    var validator_address: String? = null
    var validator_src_address: String? = null
    var validator_dst_address: String? = null
    var proposal_id: String? = null
    var voter: String? = null
    var option: String? = null
    var from: String? = null
    var to: String? = null
    var name: String? = null
    var type: String? = null
    var payload: String? = null
}
