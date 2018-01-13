package com.zigzag.whar.di

/**
 * Created by salah on 30/12/17.
 */

import com.zigzag.whar.ui.dashboard.DashboardActivity
import com.zigzag.whar.ui.dashboard.DashboardActivityModule
import com.zigzag.whar.ui.login.LoginActivity
import com.zigzag.whar.ui.login.LoginActivityModule
import com.zigzag.whar.ui.profileEdit.ProfileEditActivity
import com.zigzag.whar.ui.profileEdit.ProfileEditActivityModule

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = arrayOf(LoginActivityModule::class))
    abstract fun loginActivity(): LoginActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = arrayOf(DashboardActivityModule::class))
    abstract fun dashboardActivity(): DashboardActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = arrayOf(ProfileEditActivityModule::class))
    abstract fun profileEditActivity(): ProfileEditActivity

}