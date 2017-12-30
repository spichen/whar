package com.zigzag.whar.arch

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.os.Bundle
import android.support.annotation.CallSuper
import com.zigzag.whar.di.ActivityScoped

/**
 * Created by salah on 20/12/17.
 */

@ActivityScoped
abstract class BasePresenter<V : BaseContract.View> : LifecycleObserver, BaseContract.Presenter<V> {

    override var stateBundle: Bundle? = Bundle()

    override var view: V? = null

    override val isViewAttached: Boolean
        get() = view != null

    override fun attachLifecycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    override fun detachLifecycle(lifecycle: Lifecycle) {
        lifecycle.removeObserver(this)
    }

    override fun attachView(view: V) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }

    @CallSuper
    override fun onPresenterDestroy() {
        if (stateBundle != null && !stateBundle!!.isEmpty) {
            stateBundle!!.clear()
        }
    }

    override fun onPresenterCreated() {
        //NO-OP
    }
}