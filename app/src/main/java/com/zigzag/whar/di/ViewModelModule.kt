package com.zigzag.whar.di

import android.arch.lifecycle.ViewModelProvider
import com.zigzag.whar.common.ViewModelFactory

import dagger.Module
import dagger.Provides

/**
 * Created by salah on 21/12/17.
 */

@Module
class ViewModelModule {

    @Provides
    fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory {
        return factory
    }

}