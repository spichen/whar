package com.zigzag.whar.ui.login

import com.zigzag.whar.di.ActivityScoped
import dagger.Binds
import dagger.Module



/**
 * Created by salah on 20/12/17.
 */

@Module
abstract class LoginModule {
    @ActivityScoped
    @Binds
    internal abstract fun loginActivityPresenter(presenter: LoginPresenter): LoginContract.Presenter

/*
    @ActivityScoped
    @Binds
    internal abstract fun bindViewModelFactory(factory: WharViewModelFactory): ViewModelProvider.Factory*/
}