package com.zigzag.whar.ui.dashboard

import com.zigzag.whar.arch.BasePresenter
import javax.inject.Inject

/**
 * Created by salah on 30/12/17.
 */

class DashboardPresenter @Inject constructor() : BasePresenter<DashboardContract.View>(), DashboardContract.Presenter  {

    companion object {
        val TAG = "DashboardPresenter"
    }

}
