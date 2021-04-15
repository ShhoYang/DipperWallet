package com.highstreet.wallet.crypto

import org.bitcoinj.core.Bech32
import org.bitcoinj.crypto.*
import org.bouncycastle.crypto.digests.RIPEMD160Digest
import org.web3j.crypto.Keys
import java.io.ByteArrayOutputStream
import java.security.SecureRandom

/**
 * @author Yang Shihao
 * @Date 1/18/21
 */
object KeyUtils {

    /**
     * 生成熵
     *
     * getEntropy
     */
    fun generateEntropy(): ByteArray {
        val byteArray = ByteArray(32)
        SecureRandom().nextBytes(byteArray)
        return byteArray
    }

    /**
     * 熵转助记词
     *
     * getRandomMnemonic
     */
    @Throws(Exception::class)
    fun entropy2Mnemonic(entropy: ByteArray): List<String> {
        return MnemonicCode.INSTANCE.toMnemonic(entropy)
    }

    /**
     * 熵转助记词
     *
     * getRandomMnemonic
     */
    @Throws(Exception::class)
    fun entropy2Mnemonic(entropyAsHex: String): List<String> {
        return MnemonicCode.INSTANCE.toMnemonic(HexUtils.hexStringToBytes(entropyAsHex))
    }

    /**
     * getHDSeed
     */
    @Throws(Exception::class)
    fun mnemonic2Seed(mnemonic: List<String>): ByteArray {
        return MnemonicCode.toSeed(mnemonic, "")
    }

    /**
     * 助记词转熵
     *
     * toEntropy
     */
    @Throws(Exception::class)
    fun mnemonic2Entropy(mnemonic: List<String>): ByteArray {
        return MnemonicCode.INSTANCE.toEntropy(mnemonic)
    }

    /**
     * isValidStringHdSeedFromWords
     */
    fun isValidMnemonic(mnemonic: ArrayList<String>): Boolean {
        return try {
            val hexString = HexUtils.bytesToHexString(mnemonic2Seed(mnemonic))
            return null != hexString && isMnemonicWords(mnemonic)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * isMnemonicWords
     */
    private fun isMnemonicWords(words: ArrayList<String>): Boolean {
        val mnemonics = MnemonicCode.INSTANCE.wordList
        for (word in words) {
            if (!mnemonics.contains(word)) {
                return false
            }
        }
        return true
    }


    /**
     * 获取秘钥
     */
    fun getDeterministicKey(
        chain: String,
        mnemonic: List<String>,
        path: Int
    ): DeterministicKey {
        val deterministicKey =
            HDKeyDerivation.createMasterPrivateKey(mnemonic2Seed(mnemonic))
        return deterministicHierarchy(deterministicKey, chain, path)
    }

    /**
     * 获取秘钥
     */
    fun getDeterministicKey(chain: String, entropyAsHex: String, path: Int): DeterministicKey {
        val entropy = HexUtils.hexStringToBytes(entropyAsHex)
        val mnemonic = entropy2Mnemonic(entropy)
        val seed = mnemonic2Seed(mnemonic)
        val deterministicKey =
            HDKeyDerivation.createMasterPrivateKey(seed)
        return deterministicHierarchy(deterministicKey, chain, path)
    }

    private fun deterministicHierarchy(
        deterministicKey: DeterministicKey,
        chain: String,
        path: Int
    ): DeterministicKey {
        return DeterministicHierarchy(deterministicKey).deriveChild(
            getParentPath(chain),
            true,
            true,
            ChildNumber(path)
        )
    }

    /**
     * 老版本(11之前)是44/118
     * 新版本是44/925
     *
     * 路径不同，相同的助记词推出的地址也不一样
     */
    private fun getParentPath(chain: String): List<ChildNumber> {
        return listOf(
            ChildNumber(44, true),
            ChildNumber(925, true),
            ChildNumber.ZERO_HARDENED,
            ChildNumber.ZERO
        )
    }

    /**
     * 获取公钥
     */
    fun getPubKeyValue(key: DeterministicKey): String {
        return try {
            Base64Utils.encode(key.pubKey).replace("\n", "")
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun getAddress(chain: String, mnemonic: List<String>, path: Int): String {
        val dKey: DeterministicKey = getDeterministicKey(chain, mnemonic, path)
        val hash = SHA.hash(HexUtils.hexStringToBytes(dKey.publicKeyAsHex))
        val digest = RIPEMD160Digest()
        digest.update(hash, 0, hash.size)
        val hash2 = ByteArray(digest.digestSize)
        digest.doFinal(hash2, 0)
        return try {
            val converted = convertBits(hash2, 8, 5, true)
            Bech32.encode("dip", converted)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun getEip55Address(address: String): String {
        var bech32 = Bech32.decode(address)
        val converted = convertBits(bech32.data, 5, 8, false)
        val hex = HexUtils.bytesToHexString(converted)
        return Keys.toChecksumAddress(hex)
    }

    private fun convertBits(
        data: ByteArray,
        fromBits: Int,
        toBits: Int,
        padding: Boolean
    ): ByteArray {
        var acc = 0
        var bits = 0
        val baos = ByteArrayOutputStream()
        val maxv = (1 shl toBits) - 1
        for (i in data.indices) {
            val value: Int = data[i].toInt() and 0xff
            if (value ushr fromBits != 0) {
                throw Exception("invalid data range: data[$i]=$value (fromBits=$fromBits)")
            }
            acc = acc shl fromBits or value
            bits += fromBits
            while (bits >= toBits) {
                bits -= toBits
                baos.write(acc ushr bits and maxv)
            }
        }
        if (padding) {
            if (bits > 0) {
                baos.write(acc shl toBits - bits and maxv)
            }
        } else if (bits >= fromBits) {
            throw Exception("illegal zero padding")
        } else if (acc shl toBits - bits and maxv != 0) {
            throw Exception("non-zero padding")
        }

        val ret = baos.toByteArray()
        try {
            baos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ret
    }
}