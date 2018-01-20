package com.zigzag.whar.arch

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.os.Bundle
import android.support.annotation.CallSuper
import com.zigzag.whar.di.ActivityScoped
import com.zigzag.whar.rx.firebase.RxFirebaseAuth
import com.zigzag.whar.rx.firebase.RxFirebaseFirestore
import com.zigzag.whar.rx.firebase.RxFirebaseStorage
import io.reactivex.disposables.CompositeDisposable
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

    @Inject
    lateinit var rxFirebaseFirestore : RxFirebaseFirestore

    var compositeDisposables : CompositeDisposable = CompositeDisposable()

    @Suppress("NOTHING_TO_INLINE")
    inline fun Disposable.track() = compositeDisposables.add(this)

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
        compositeDisposables.clear()
    }

    override fun onPresenterCreated() {
        rxFirebaseAuth.observeAuthState()
                .subscribe { firebaseAuth ->
                    view?.authRedirect(firebaseAuth.currentUser)
                    /*val user = firebaseAuth.currentUser
                    if(user==null){
                        view?.logout()
                    }else{
                        if(user.displayName.isNullOrEmpty())
                            view?.gotoEditProfile()
                    }*/
                }
    }
}