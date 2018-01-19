package com.zigzag.whar.ui.dashboard

import com.zigzag.whar.di.ActivityScoped
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.zigzag.whar.di.FragmentScoped

/**
 * Created by salah on 20/12/17.
 */

@Module
abstract class DashboardModule {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun dashboardFragment(): DashboardFragment

    @ActivityScoped
    @Binds
    internal abstract fun dashboardPresenter(presenter: DashboardPresenter): DashboardContract.Presenter

}