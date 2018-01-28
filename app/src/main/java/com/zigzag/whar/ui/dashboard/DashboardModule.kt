package com.zigzag.whar.ui.dashboard

import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.zigzag.whar.di.FragmentScoped
import com.zigzag.whar.ui.dashboard.feeds.FeedsFragment
import com.zigzag.whar.ui.dashboard.fragments.PermissionFragment

/**
 * Created by salah on 20/12/17.
 */

@Module
abstract class DashboardModule {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun feedsFragment(): FeedsFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun permissionFragment(): PermissionFragment

}