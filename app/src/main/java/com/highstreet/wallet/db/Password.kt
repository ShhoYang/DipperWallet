package com.highstreet.wallet.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.constant.Constant
import com.highstreet.wallet.crypto.RSA

/**
 * @author Yang Shihao
 * @Date 1/20/21
 */

@Entity
class Password(
    @PrimaryKey()
    var id: Long,
    var resource: String,
    var spec: String,
    var fingerprint: Boolean
) {

    fun verify(input: String): Boolean {
        return RSA.verify(input, resource, Constant.PASSWORD_KEYSTORE_ALIAS)
    }
}