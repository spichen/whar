package com.zigzag.whar.ui.dashboard

import com.zigzag.whar.di.ActivityScoped
import com.zigzag.whar.ui.dashboard.DashboardActivityContract
import com.zigzag.whar.ui.dashboard.DashboardActivityPresenter
import dagger.Binds
import dagger.Module

/**
 * Created by salah on 20/12/17.
 */

@Module
abstract class DashboardActivityModule {

    @ActivityScoped
    @Binds
    internal abstract fun dashboardActivityPresenter(presenter: DashboardActivityPresenter): DashboardActivityContract.Presenter

}