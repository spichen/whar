package com.zigzag.whar.arch

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.View
import com.zigzag.whar.ui.dashboard.DashboardActivity
import com.zigzag.whar.ui.login.LoginActivity
import com.zigzag.whar.ui.profileEdit.ProfileEditActivity

import dagger.android.support.DaggerFragment

/**
 * Created by salah on 27/12/17.
 */

abstract class BaseFragment<V : BaseContract.View, P : BaseContract.Presenter<V>> : DaggerFragment(), BaseContract.View {

    private val lifecycleRegistry = LifecycleRegistry(this)
    protected lateinit var presenter: P
    private var isPresenterCreated = false

    @CallSuper
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachLifecycle(lifecycle)
        presenter.detachView()
    }

    protected abstract fun getPresenterImpl(): P


    override fun logout() {
        startActivity(Intent(activity, LoginActivity::class.java))
        activity.finish()
    }

    override fun gotoDashboard() {
        startActivity(Intent(activity, DashboardActivity::class.java))
        activity.finish()
    }

    override fun gotoEditProfile() {
        startActivity(Intent(activity, ProfileEditActivity::class.java))
        activity.finish()
    }
}
