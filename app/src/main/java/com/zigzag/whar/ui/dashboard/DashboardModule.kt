package com.zigzag.whar.ui.dashboard

import com.zigzag.whar.di.ActivityScoped
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.zigzag.whar.di.FragmentScoped
import com.zigzag.whar.ui.dashboard.feeds.FeedsFragment

/**
 * Created by salah on 20/12/17.
 */

@Module
abstract class DashboardModule {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun feedsFragment(): FeedsFragment

    @ActivityScoped
    @Binds
    internal abstract fun dashboardPresenter(presenter: DashboardPresenter): DashboardContract.Presenter

}