package com.highstreet.wallet.constant

/**
 * @author Yang Shihao
 *
 * 链名
 */
enum class Chain(val chainName: String) {
    ALL("ALL"),
    DIP_TEST("dip-testnet"),
    DIP_MAIN("dipperhub");

    companion object {
        fun getChain(chainName: String): Chain {
            return when (chainName) {
                DIP_MAIN.chainName -> DIP_MAIN
                else -> DIP_TEST
            }
        }
    }
}