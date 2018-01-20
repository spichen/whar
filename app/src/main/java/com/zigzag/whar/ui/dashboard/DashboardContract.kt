package com.zigzag.whar.ui.dashboard

import com.zigzag.whar.arch.BaseContract

/**
 * Created by salah on 30/12/17.
 */

class DashboardContract {
    interface View : BaseContract.View {
        fun logout()
        fun gotoEditProfile()
    }

    interface Presenter : BaseContract.Presenter<View>

}
