package com.zigzag.whar.arch

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.annotation.CallSuper
import com.google.firebase.auth.FirebaseAuth
import com.zigzag.whar.di.ActivityScoped
import com.zigzag.whar.rx.firebase.RxFirebaseAuth
import com.zigzag.whar.rx.firebase.RxFirebaseStorage
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * Created by salah on 20/12/17.
 */

@ActivityScoped
abstract class BasePresenter<V : BaseContract.View> : LifecycleObserver, BaseContract.Presenter<V> {

    @Inject
    lateinit var rxFirebaseAuth : RxFirebaseAuth

    @Inject
    lateinit var rxFirebaseStorage : RxFirebaseStorage

    override var stateBundle: Bundle? = Bundle()

    override var view: V? = null

    override val isViewAttached: Boolean
        get() = view != null

    lateinit var authObserver : Disposable

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
        authObserver.dispose()
    }

    override fun onPresenterCreated() {
        authObserver = rxFirebaseAuth.observeAuthState()
                .subscribe { firebaseAuth ->
                    val user = firebaseAuth.currentUser
                    if(user==null){
                        view?.logout()
                    }else{
                        if(user.displayName.isNullOrEmpty())
                            view?.gotoEditProfile()
                        else
                            view?.gotoDashboard()
                    }
                }
    }
}