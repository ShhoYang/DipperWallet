package com.highstreet.wallet.ui.vm

import androidx.lifecycle.MutableLiveData
import com.hao.library.utils.CoroutineUtils
import com.hao.library.viewmodel.BaseViewModel
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.constant.Constant
import com.highstreet.wallet.crypto.RSA
import com.highstreet.wallet.db.Password
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author Yang Shihao
 * @Date 2020/10/22
 */

class CreatePasswordVM : BaseViewModel() {

    val createPasswordLD = MutableLiveData<Password?>()
    val fingerprintLD = MutableLiveData<Boolean>()

    fun createPassword(password: String) {
        CoroutineUtils.io2main({
            try {
                val signPass = RSA.sign(password, Constant.PASSWORD_KEYSTORE_ALIAS)
                if (null == signPass) {
                    null
                } else {
                    val newPw = Password(Constant.PASSWORD_DEFAULT_ID, signPass, "", false)
                    if (AccountManager.instance().updatePassword(newPw)) {
                        newPw
                    } else {
                        null
                    }
                }
            } catch (e: Exception) {
                null
            }
        }, {
            createPasswordLD.value = it
        })
    }

    fun setFingerprint(password: Password) {
        Observable.create(ObservableOnSubscribe<Boolean> {
            password.fingerprint = true
            it.onNext(AccountManager.instance().updatePassword(password))
            it.onComplete()
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                fingerprintLD.value = it
            }, {
                it.printStackTrace()
                fingerprintLD.value = false
            }).add()
    }
}