package com.highstreet.wallet.model

import com.google.gson.Gson
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.constant.Constant
import com.highstreet.wallet.crypto.HexUtils
import com.highstreet.wallet.crypto.KeyUtils
import org.bitcoinj.crypto.DeterministicKey
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Yang Shihao
 * @Date 2020/10/22
 */


class WalletParams(
    val china: String,
    val uuid: String,
    var nickName: String,
    val address: String,
    val entropy: ByteArray,
    val entropyAsHex: String,
    val mnemonic: List<String>,
    val mnemonicSize: Int,
    val path: Int,
    val fromMnemonic: Boolean

) {

    override fun toString(): String {
        return Gson().toJson(this)
    }

    companion object {
        /**
         * 1、生成熵（lib）
         * 2、根据熵和path得到公钥
         * 3、把公钥转为ByteArray
         * 4、进行SHA256签名
         * 5、进行RIPEMD160签名
         * 6、位转换
         * 7、bech32编码
         */
        fun create(): WalletParams {
            val entropy = KeyUtils.generateEntropy()
            val entropyAsHex = HexUtils.bytesToHexString(entropy)
            val mnemonic = KeyUtils.entropy2Mnemonic(entropy)
            val chain = AccountManager.instance().chain
            val address = KeyUtils.getAddress(chain, mnemonic, Constant.PATH)
            return WalletParams(
                chain,
                UUID.randomUUID().toString(),
                "",
                address,
                entropy,
                entropyAsHex,
                mnemonic,
                Constant.MNEMONIC_SIZE,
                Constant.PATH,
                false
            )
        }

        fun import(mnemonic: ArrayList<String>): WalletParams {
            val entropy = KeyUtils.mnemonic2Entropy(mnemonic)
            val entropyAsHex = HexUtils.bytesToHexString(entropy)
            val chain = AccountManager.instance().chain
            val address = KeyUtils.getAddress(chain, mnemonic, Constant.PATH)
            return WalletParams(
                chain,
                UUID.randomUUID().toString(),
                "",
                address,
                entropy,
                entropyAsHex,
                mnemonic,
                Constant.MNEMONIC_SIZE,
                Constant.PATH,
                true
            )
        }
    }
}