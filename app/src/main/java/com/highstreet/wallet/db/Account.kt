package com.highstreet.wallet.db

import android.text.TextUtils
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.highstreet.lib.adapter.BaseItem
import com.highstreet.wallet.constant.Constant
import com.highstreet.wallet.crypto.AES
import java.io.Serializable

/**
 * @author Yang Shihao
 * @Date 1/20/21
 */
@Entity
data class Account(
    @PrimaryKey(autoGenerate = true)
    var id: Long?,
    var uuid: String,
    var nickName: String,
    var isValidator: Boolean,

    var address: String,
    var chain: String,
    var path: Int,

    var resource: String,
    var spec: String,
    var mnemonicSize: Int,
    var fromMnemonic: Boolean,

    val balance: String,
    var sequenceNumber: Int,
    var accountNumber: Int,

    var hasPrivateKey: Boolean,
    var isFavorite: Boolean,
    var isBackup: Boolean,
    var pushAlarm: Boolean,
    var fingerprint: Boolean,
    var isLast: Boolean,
    var createTime: Long,
    var importTime: Long,
    var sort: Int,
    var extension: String
) : BaseItem, Serializable {

    @Ignore
    private var entropy: String? = null

    fun getEntropyAsHex(): String {
        if (TextUtils.isEmpty(entropy)) {
            entropy = AES.decrypt(
                resource,
                spec,
                Constant.MNEMONIC_KEYSTORE_ALIAS + uuid,
            ) ?: ""
        }
        return entropy!!
    }


    @Ignore
    private var upperCaseChain: String? = null

    /**
     * 大写链名
     */
    fun getUpperCaseChainName(): String {
        if (null == upperCaseChain) {
            upperCaseChain = if (TextUtils.isEmpty(chain)) {
                ""
            } else {
                chain.replace("-", " ").toUpperCase()
            }
        }

        return upperCaseChain!!
    }

    override fun uniqueKey(): String {
        return uuid
    }

}
