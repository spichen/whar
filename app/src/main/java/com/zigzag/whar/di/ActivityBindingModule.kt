package com.zigzag.whar.di

/**
 * Created by salah on 30/12/17.
 */

import com.zigzag.whar.arch.BaseActivity
import com.zigzag.whar.ui.dashboard.DashboardActivity
import com.zigzag.whar.ui.dashboard.DashboardModule
import com.zigzag.whar.ui.login.LoginActivity
import com.zigzag.whar.ui.login.LoginModule
import com.zigzag.whar.ui.profileEdit.ProfileEditActivity
import com.zigzag.whar.ui.profileEdit.ProfileEditModule

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = arrayOf(LoginModule::class))
    abstract fun loginActivity(): LoginActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = arrayOf(DashboardModule::class))
    abstract fun dashboardActivity(): DashboardActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = arrayOf(ProfileEditModule::class))
    abstract fun profileEditActivity(): ProfileEditActivity

}