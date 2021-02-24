package com.highstreet.wallet.model.req

import com.google.gson.GsonBuilder

data class SignatureMsg(
    var chain_id: String,
    var account_number: String,
    var sequence: String,
    var msgs: ArrayList<Msg>,
    var fee: Fee,
    var memo: String
) {

    fun toByteArray(): ByteArray {
        val gson = GsonBuilder().disableHtmlEscaping().create()
        return gson.toJson(this).toByteArray(Charsets.UTF_8)
    }
}