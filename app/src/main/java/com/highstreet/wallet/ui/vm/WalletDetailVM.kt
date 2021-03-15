package com.highstreet.wallet.ui.vm

import androidx.lifecycle.MutableLiveData
import com.hao.library.viewmodel.BaseViewModel
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.db.Account
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author Yang Shihao
 * @Date 2020/10/22
 */
class WalletDetailVM : BaseViewModel() {

    val updateNameLD = MutableLiveData<Boolean>()
    val deleteLD = MutableLiveData<Boolean>()

    fun updateWalletName(account: Account, newName: String) {
        if (account.nickName == newName) {
            return
        }
        account.nickName = newName
        Observable.create(ObservableOnSubscribe<Boolean> {
            it.onNext(AccountManager.instance().update(account))
            it.onComplete()
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                updateNameLD.value = it
            }, {
                it.printStackTrace()
                updateNameLD.value = false
            }).add()
    }

    fun deleteAccount(account: Account) {
        Observable.create(ObservableOnSubscribe<Boolean> {
            it.onNext(AccountManager.instance().deleteAccount(account))
            it.onComplete()
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                deleteLD.value = it
            }, {
                it.printStackTrace()
                deleteLD.value = false
            }).add()
    }
}
