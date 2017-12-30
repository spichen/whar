package com.zigzag.whar.di

import com.zigzag.whar.ui.login.LoginActivity
import com.zigzag.whar.ui.login.LoginActivityModule

import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by salah on 20/12/17.
 */

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = arrayOf(LoginActivityModule::class))
    internal abstract fun loginActivity(): LoginActivity

}