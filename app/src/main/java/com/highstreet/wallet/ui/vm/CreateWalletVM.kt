package com.highstreet.wallet.ui.vm

import androidx.lifecycle.MutableLiveData
import com.hao.library.http.subscribeBy2
import com.hao.library.viewmodel.BaseViewModel
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.BuildConfig
import com.highstreet.wallet.constant.Constant
import com.highstreet.wallet.crypto.AES
import com.highstreet.wallet.db.Account
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.model.WalletParams
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */

class CreateWalletVM : BaseViewModel() {

    val resultLD = MutableLiveData<Boolean>()

    private var accountManager = AccountManager.instance()

    fun createWallet(walletParams: WalletParams) {
        Observable.create(ObservableOnSubscribe<Boolean> {
            val account = generateAccount(walletParams)
            it.onNext(accountManager.addAccount(account))
            it.onComplete()
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it) {
                    test()
                } else {
                    resultLD.value = false
                }
            }, {
                it.printStackTrace()
                resultLD.value = false

            }).add()
    }

    private fun generateAccount(walletParams: WalletParams): Account {
        val encR = AES.encrypt(
            walletParams.entropyAsHex,
            Constant.MNEMONIC_KEYSTORE_ALIAS + walletParams.uuid
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
            createTime = System.currentTimeMillis(),
            importTime = 0,
            sort = 0,
            extension = ""
        )
    }

    /**
     * 水龙头
     */
    private fun test() {
        if (!BuildConfig.testnet) {
            resultLD.value = true
            return
        }

        ApiService.getApi()
            .test("https://faucet.testnet.dippernetwork.com/get_token?" + accountManager.address)
            .subscribeBy2({
                resultLD.value = true
            }, {
                resultLD.value = true
            }).add()
    }
}