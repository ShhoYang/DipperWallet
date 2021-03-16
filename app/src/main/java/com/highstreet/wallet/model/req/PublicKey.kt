package com.highstreet.wallet.model.req

import java.io.Serializable

/**
 * @author Yang Shihao
 */
data class PublicKey(
    val type: String?,
    val value: String?
) : Serializable