package com.zigzag.whar.di

import com.zigzag.whar.ui.login.LoginProcessor

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

/**
 * Created by salah on 21/12/17.
 */

@Module
class ProcessorModule {

    @Provides
    @Singleton
    internal fun provideLoginProcessor(): LoginProcessor {
        return LoginProcessor()
    }

}