package com.zigzag.whar.ui.dashboard

import com.zigzag.whar.old.BaseContract

/**
 * Created by salah on 30/12/17.
 */

class DashboardContract {
    interface View : BaseContract.View {
        fun displayPermissionFragment()
        fun displayFeedsFragment()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun displayPermissionFragment()
        fun displayFeedsFragment()
    }

}
