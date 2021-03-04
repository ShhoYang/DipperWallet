package com.highstreet.wallet.ui.vm

import androidx.lifecycle.MutableLiveData
import com.hao.library.viewmodel.BaseViewModel
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.constant.Constant
import com.highstreet.wallet.crypto.AES
import com.highstreet.wallet.db.Account
import com.highstreet.wallet.model.WalletParams
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author Yang Shihao
 * @Date 2020/10/22
 */

class ImportWalletVM : BaseViewModel() {

    val resultLD = MutableLiveData<Boolean>()

    fun importWallet(walletParams: WalletParams) {
        Observable.create(ObservableOnSubscribe<Boolean> {
            val accountManager = AccountManager.instance()
            val account = generateAccount(walletParams)
            account.isBackup = true
            it.onNext(accountManager.addAccount(account))
            it.onComplete()
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resultLD.value = it
            }, {
                it.printStackTrace()
                resultLD.value = null

            }).add()
    }

    private fun generateAccount(walletParams: WalletParams): Account {
        val encR = AES.encrypt(
            walletParams.entropyAsHex,
            Constant.MNEMONIC_KEYSTORE_ALIAS + walletParams.uuid,

        )
        return Account(
            id = null,
            uuid = walletParams.uuid,
            nickName = walletParams.nickName,
            isValidator = false,
            address = walletParams.address,
            chain = walletParams.china,
            path = walletParams.path,
            resource = encR?.first ?: "",
            spec = encR?.second ?: "",
            mnemonicSize = walletParams.mnemonicSize,
            fromMnemonic = walletParams.fromMnemonic,
            balance = "",
            sequenceNumber = 0,
            accountNumber = 0,
            hasPrivateKey = true,
            isFavorite = false,
            isBackup = false,
            pushAlarm = false,
            fingerprint = false,
            isLast = true,
            createTime = 0,
            importTime = System.currentTimeMillis(),
            sort = 0,
            extension = ""
        )
    }
}