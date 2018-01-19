package com.zigzag.whar.ui.dashboard

import com.zigzag.whar.arch.BaseContract
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider

/**
 * Created by salah on 30/12/17.
 */

class DashboardContract {
    interface View : BaseContract.View {
        fun tempShowLocation(location : String)
        fun displayFeeds()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun initiate(locationProvider : ReactiveLocationProvider)
    }

}
