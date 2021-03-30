package com.highstreet.wallet.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * @author Yang Shihao
 * @Date 3/6/21
 */
@Entity
data class Token(
    @PrimaryKey(autoGenerate = true)
    var id: Long?,
    var isNative: Boolean,
    val chain: String,
    val icon: String,
    val name: String,
    val desc: String,
    val decimalPlaces: Int,
    val address: String,
    val symbol: String,
    var balance: String,
    var balanceAmount: String,
    var delegateAmount: String,
    var unbondingAmount: String,
    var reward: String,
    var extension: String
) : Serializable
