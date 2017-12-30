package com.zigzag.whar.di

/**
 * Created by salah on 30/12/17.
 */

import com.zigzag.whar.ui.login.LoginActivity
import com.zigzag.whar.ui.login.LoginActivityModule

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = arrayOf(LoginActivityModule::class))
    internal abstract fun loginActivity(): LoginActivity

}