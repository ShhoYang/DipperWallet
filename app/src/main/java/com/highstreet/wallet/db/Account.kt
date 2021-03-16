package com.highstreet.wallet.db

import android.text.TextUtils
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.hao.library.adapter.PagedAdapterItem
import com.highstreet.wallet.R
import com.highstreet.wallet.constant.Chain
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

    var balance: String,
    val delegateAmount: String,
    val unbondingAmount: String,
    val reward: String,
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
) : PagedAdapterItem, Serializable {

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

    fun isMain(): Boolean {
        return Chain.DIP_MAIN.chainName == chain
    }

    fun isTest(): Boolean {
        return Chain.DIP_TEST.chainName == chain
    }

    fun getIcon(): Int {
        return if (isMain()) {
            R.mipmap.dipper_hub
        } else if (isTest()) {
            R.mipmap.dipper_test
        } else {
            0
        }
    }

    override fun getKey(): String {
        return uuid
    }

    companion object {
        fun empty(chain: Chain): Account {
            return Account(
                id = null,
                uuid = "",
                nickName = "",
                isValidator = false,
                address = "",
                chain = chain.chainName,
                path = 0,
                resource = "",
                spec = "",
                mnemonicSize = Constant.MNEMONIC_SIZE,
                fromMnemonic = false,
                balance = "",
                delegateAmount = "",
                unbondingAmount = "",
                reward = "",
                sequenceNumber = 0,
                accountNumber = 0,
                hasPrivateKey = true,
                isFavorite = false,
                isBackup = false,
                pushAlarm = false,
                fingerprint = false,
                isLast = true,
                createTime = 0,
                importTime = 0,
                sort = 0,
                extension = ""
            )
        }
    }
}
