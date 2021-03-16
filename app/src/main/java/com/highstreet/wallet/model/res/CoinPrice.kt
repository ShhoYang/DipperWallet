package com.highstreet.wallet.model.res

import com.google.gson.annotations.SerializedName

/**
 * @author Yang Shihao
 * @Date 3/16/21
 */
data class CoinPrice(

    @SerializedName("dipper-network")
    var price: Map<String, String>
)