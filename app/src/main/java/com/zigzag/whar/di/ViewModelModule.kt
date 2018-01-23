package com.zigzag.whar.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.zigzag.whar.ViewModelFactory
import com.zigzag.whar.rx.firebase.RxFirebaseAuth
import com.zigzag.whar.rx.firebase.RxFirebaseFirestore
import com.zigzag.whar.rx.firebase.RxFirebaseStorage
import com.zigzag.whar.ui.login.LoginViewModel

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

/**
 * Created by salah on 21/12/17.
 */

@Module
class ViewModelModule {

    @Provides
    fun provideLoginViewModel(viewModel: LoginViewModel): ViewModel {
        return viewModel
    }

    @Provides
    fun provideWharViewModelFactory(
            factory: ViewModelFactory
    ): ViewModelProvider.Factory {
        return factory
    }
}