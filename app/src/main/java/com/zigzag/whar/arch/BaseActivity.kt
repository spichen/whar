package com.zigzag.whar.arch

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.inputmethod.InputMethodManager

import dagger.android.support.DaggerAppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics



/**
 * Created by salah on 27/12/17.
 */

abstract class BaseActivity<V : BaseContract.View, P : BaseContract.Presenter<V>> : DaggerAppCompatActivity(), BaseContract.View {

    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private val lifecycleRegistry = LifecycleRegistry(this)

    protected lateinit var presenter: P
    private var isPresenterCreated = false
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val viewModel = ViewModelProviders.of(this).get(BaseViewModel<V,P>()::class.java)
        if (viewModel.presenter == null) {
            viewModel.presenter = initPresenter()
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
    }

    protected abstract fun initPresenter(): P


    fun hideKeyboard(){
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
