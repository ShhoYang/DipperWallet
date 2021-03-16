package com.highstreet.wallet.constant

import com.highstreet.wallet.AccountManager

/**
 * @author Yang Shihao
 * @Date 2020/10/23
 */
object Constant {

    const val BASE_URL_MAIN = "https://rpc.dippernetwork.com/"
    const val BASE_URL_TEST = "https://rpc.testnet.dippernetwork.com/"

    fun getBaseUrl(): String {
        return when (Chain.getChain(AccountManager.instance().chain)) {
            Chain.DIP_MAIN -> BASE_URL_MAIN
            else -> BASE_URL_TEST
        }
    }

    const val PATH = 0
    const val MNEMONIC_SIZE = 24
    const val GAS = 400_000
    const val GAS_PRICE = 600_000

    const val DIP_RATE = 1_000_000_000_000

    const val PASSWORD_DEFAULT_ID = 1L

    const val KEY_TYPE_PUBLIC = "tendermint/PubKeySecp256k1"

    // 别名
    const val PASSWORD_KEYSTORE_ALIAS = "PASSWORD_KEY"
    const val MNEMONIC_KEYSTORE_ALIAS = "MNEMONIC_KEY"

    const val DIPPER_NETWORK = "https://www.dippernetwork.com/"

    const val WALLET_PATH = "m/44'/925'/0'/0/0"

}