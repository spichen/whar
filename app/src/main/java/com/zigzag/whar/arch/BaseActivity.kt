package com.zigzag.whar.arch

import android.arch.lifecycle.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.inputmethod.InputMethodManager

import dagger.android.support.DaggerAppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.zigzag.whar.ui.dashboard.DashboardActivity
import com.zigzag.whar.ui.login.LoginActivity
import com.zigzag.whar.ui.profileEdit.ProfileEditActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by salah on 27/12/17.
 */

abstract class BaseActivity<V : BaseContract.View, P : BaseContract.Presenter<V>> : DaggerAppCompatActivity(), BaseContract.View{

    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private val lifecycleRegistry = LifecycleRegistry(this)
    protected lateinit var presenter: P
    private var isPresenterCreated = false

    var compositeDisposables : CompositeDisposable = CompositeDisposable()

    @Suppress("NOTHING_TO_INLINE")
    inline fun Disposable.track() = compositeDisposables.add(this)

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val viewModel = ViewModelProviders.of(this).get(BaseViewModel<V,P>()::class.java)
        if (viewModel.presenter == null) {
            viewModel.presenter = getPresenterImpl()
            isPresenterCreated = true
        }
        presenter = viewModel.presenter as P
        presenter.attachLifecycle(lifecycle)
        presenter.attachView(this as V)
        if (isPresenterCreated)
            presenter.onPresenterCreated()
    }

    override fun getLifecycle(): LifecycleRegistry {
        return lifecycleRegistry
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        presenter.detachLifecycle(lifecycle)
        presenter.detachView()
        compositeDisposables.clear()
    }

    protected abstract fun getPresenterImpl(): P


    fun hideKeyboard(){
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun logout() {
        startActivity(Intent(this@BaseActivity,LoginActivity::class.java))
        finish()
    }

    override fun gotoDashboard() {
        startActivity(Intent(this@BaseActivity,DashboardActivity::class.java))
        finish()
    }

    override fun gotoEditProfile() {
        startActivity(Intent(this@BaseActivity,ProfileEditActivity::class.java))
        finish()
    }

}
