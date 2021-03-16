package com.highstreet.wallet.model.req

import java.io.Serializable

data class RequestBroadCast(
        val mode: String,
        val tx: StdTxValue
)

data class StdTx(
        var type: String,
        var value: StdTxValue): Serializable


class StdTxValue(
        val msg: ArrayList<Msg>,
        var fee: Fee,
        var signatures: ArrayList<Signature>,
        var memo: String
): Serializable


