package com.highstreet.wallet.crypto

import org.bitcoinj.crypto.*
import org.bouncycastle.crypto.digests.RIPEMD160Digest
import java.io.ByteArrayOutputStream
import java.security.SecureRandom

/**
 * @author Yang Shihao
 * @Date 1/18/21
 */
object KeyUtils {

    private const val CHARSET = "qpzry9x8gf2tvdw0s3jn54khce6mua7l"

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
            Base64Utils.encodeToString(key.pubKey).replace("\n", "")
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * getDpAddress
     */
    fun getAddress(chain: String, mnemonic: List<String>, path: Int): String {
        val dKey: DeterministicKey = getDeterministicKey(chain, mnemonic, path)
        val hash = SHA.hash(HexUtils.hexStringToBytes(dKey.publicKeyAsHex))
        val digest = RIPEMD160Digest()
        digest.update(hash, 0, hash.size)
        val hash2 = ByteArray(digest.digestSize)
        digest.doFinal(hash2, 0)
        return try {
            val converted = convertBits(hash2, 8, 5, true)
            bech32Encode("dip".toByteArray(), converted)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }


    private fun convertBits(data: ByteArray, fromBits: Int, toBits: Int, pad: Boolean): ByteArray {
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
        if (pad) {
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

    private fun bech32Encode(hrp: ByteArray, data: ByteArray): String {
        val chk = createChecksum(hrp, data)
        val combined = ByteArray(chk.size + data.size)
        System.arraycopy(data, 0, combined, 0, data.size)
        System.arraycopy(chk, 0, combined, data.size, chk.size)
        val xlat = ByteArray(combined.size)
        for (i in combined.indices) {
            xlat[i] = CHARSET[combined[i].toInt()].toByte()
        }
        val ret = ByteArray(hrp.size + xlat.size + 1)
        System.arraycopy(hrp, 0, ret, 0, hrp.size)
        System.arraycopy(byteArrayOf(0x31), 0, ret, hrp.size, 1)
        System.arraycopy(xlat, 0, ret, hrp.size + 1, xlat.size)
        return String(ret)
    }

    private fun createChecksum(hrp: ByteArray, data: ByteArray): ByteArray {
        val zeroes = byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00)
        val expanded = hrpExpand(hrp)
        val values = ByteArray(zeroes.size + expanded.size + data.size)
        System.arraycopy(expanded, 0, values, 0, expanded.size)
        System.arraycopy(data, 0, values, expanded.size, data.size)
        System.arraycopy(zeroes, 0, values, expanded.size + data.size, zeroes.size)
        val polymod = polymod(values) xor 1
        val ret = ByteArray(6)
        for (i in ret.indices) {
            ret[i] = (polymod shr 5 * (5 - i) and 0x1f).toByte()
        }
        return ret
    }

    private fun polymod(values: ByteArray): Int {
        val GENERATORS = intArrayOf(0x3b6a57b2, 0x26508e6d, 0x1ea119fa, 0x3d4233dd, 0x2a1462b3)
        var chk = 1
        for (b in values) {
            val top = (chk shr 0x19).toByte()
            chk = b.toInt() xor (chk and 0x1ffffff shl 5)
            for (i in 0..4) {
                chk = chk xor if (top.toInt() shr i and 1 == 1) GENERATORS[i] else 0
            }
        }
        return chk
    }

    private fun hrpExpand(hrp: ByteArray): ByteArray {
        val buf1 = ByteArray(hrp.size)
        val buf2 = ByteArray(hrp.size)
        val mid = ByteArray(1)
        for (i in hrp.indices) {
            buf1[i] = (hrp[i].toInt() shr 5).toByte()
        }
        mid[0] = 0x00
        for (i in hrp.indices) {
            buf2[i] = (hrp[i].toInt() and 0x1f).toByte()
        }
        val ret = ByteArray(hrp.size * 2 + 1)
        System.arraycopy(buf1, 0, ret, 0, buf1.size)
        System.arraycopy(mid, 0, ret, buf1.size, mid.size)
        System.arraycopy(buf2, 0, ret, buf1.size + mid.size, buf2.size)
        return ret
    }
}