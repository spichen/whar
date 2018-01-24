package com.zigzag.whar.ui.login

import android.arch.lifecycle.ViewModel
import com.zigzag.whar.di.ActivityScoped
import dagger.Binds
import dagger.Module
import dagger.Provides


/**
 * Created by salah on 20/12/17.
 */

@Module
abstract class LoginModule {

    @ActivityScoped
    @Binds
    abstract fun provideLoginViewModel(viewModel: LoginViewModel): ViewModel
/*
    @ActivityScoped
    @Binds
    internal abstract fun loginActivityPresenter(presenter: LoginPresenter): LoginContract.Presenter

    @ActivityScoped
    @Binds
    internal abstract fun bindViewModelFactory(factory: WharViewModelFactory): ViewModelProvider.Factory*/
}