package com.highstreet.wallet.model.req

import java.io.Serializable

data class Signature(
    val pub_key: PublicKey,
    val signature: String,
    val account_number: String,
    val sequence: String
) : Serializable