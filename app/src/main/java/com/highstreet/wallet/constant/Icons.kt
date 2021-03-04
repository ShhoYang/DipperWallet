package com.highstreet.wallet.constant

import kotlin.collections.ArrayList

/**
 * @author Yang Shihao
 * @Date 2/25/21
 */
object Icons {
    const val QINIU_URL = "http://dev-p7-supplychain.highstreet.top/"
    val list = arrayOf(
        "ADA.png",
        "BCH.png",
        "BNB.png",
        "BTC.png",
        "DOGE.png",
        "EOS.png",
        "ETH.png",
        "LTC.png",
        "USDT.png",
        "XEM.png",
        "XLM.png",
        "XMR.png",
        "XRP.png"
    )

    fun get(index: Int): String {
        return QINIU_URL + list[index % list.size]
    }
}

fun main() {
    val list = ArrayList<String>()
    Icons.list.forEach {
        list.add(it)
    }
    list.sort()
    list.forEach {
        println("\"${it}\",")
    }
}