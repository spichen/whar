package com.zigzag.whar.ui.dashboard

import com.zigzag.whar.arch.BaseContract

/**
 * Created by salah on 30/12/17.
 */

class DashboardActivityContract {
    interface View : BaseContract.View

    interface Presenter : BaseContract.Presenter<View>

}
