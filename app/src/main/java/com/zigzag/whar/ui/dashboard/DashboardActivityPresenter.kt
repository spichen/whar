package com.zigzag.whar.ui.dashboard

import com.zigzag.whar.arch.BasePresenter
import javax.inject.Inject

/**
 * Created by salah on 30/12/17.
 */

class DashboardActivityPresenter @Inject constructor() : BasePresenter<DashboardActivityContract.View>(), DashboardActivityContract.Presenter  {

    val TAG = "DashboardActivityPresenter"
}
