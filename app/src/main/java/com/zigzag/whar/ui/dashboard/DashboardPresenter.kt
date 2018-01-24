package com.zigzag.whar.ui.dashboard

import com.zigzag.whar.old.BasePresenter
import com.zigzag.whar.di.ActivityScoped
import javax.inject.Inject

/**
 * Created by salah on 30/12/17.
 */

@ActivityScoped
class DashboardPresenter @Inject constructor() : BasePresenter<DashboardContract.View>(), DashboardContract.Presenter  {

    companion object {
        val TAG = "DashboardPresenter"
    }

    override fun displayPermissionFragment() {
        view?.displayPermissionFragment()
    }

    override fun displayFeedsFragment() {
        view?.displayFeedsFragment()
    }


}
