package com.highstreet.lib.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.highstreet.lib.BaseApplication
import com.highstreet.lib.utils.L
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel(), LifecycleObserver {

    private var tag = "BaseViewModel_${javaClass.simpleName}"

    var needRefreshInResume = false

    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    /**
     * 注册RxBus,和needRefreshInResume配合使用
     */
    fun <T> registerRxBus(cls: Class<T>) {
        RxBus.instance().register(cls).subscribe({
            L.d(tag, "registerRxBus#subscribe")
            needRefreshInResume = true
        }, {}).add()
    }

    fun Disposable.add() {
        compositeDisposable.add(this)
    }

    open fun refreshOnResume() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    protected open fun onCreate() {
        L.d(tag, "onCreate")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected open fun onStart() {
        L.d(tag, "onStart")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected open fun onResume() {
        L.d(tag, "onResume")
        if (needRefreshInResume) {
            needRefreshInResume = false
            refreshOnResume()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected open fun onPause() {
        L.d(tag, "onPause")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected open fun onStop() {
        L.d(tag, "onStop")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected open fun onDestroy() {
        L.d(tag, "onDestroy")
    }

    override fun onCleared() {
        super.onCleared()
        L.d(tag, "onCleared")
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    protected fun getString(@StringRes resId: Int): String {
        return BaseApplication.instance.getString(resId)
    }
}
