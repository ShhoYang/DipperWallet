package com.highstreet.wallet.constant

/**
 * @author Yang Shihao
 *
 * 链名
 */
enum class Chain(val chainName: String) {
    ALL("ALL"),
    DIP_TEST("dip-testnet"),
    DIP_MAIN("dipperhub"),
    DIP_TEST2("DIPPER_TEST"),
    DIP_MAIN2("DIPPER_HUB");

    companion object {
        fun getChain(chainName: String): Chain {
            return when (chainName) {
                DIP_MAIN.chainName, DIP_MAIN2.chainName -> DIP_MAIN2
                else -> DIP_TEST2
            }
        }
    }
}