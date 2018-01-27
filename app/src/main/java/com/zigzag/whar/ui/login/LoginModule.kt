package com.zigzag.whar.ui.login

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * Created by salah on 20/12/17.
 */

@Module
abstract class LoginModule {

    @Binds
    @Singleton
    abstract fun provideLoginViewModel(viewModel: LoginViewModel): ViewModel

}