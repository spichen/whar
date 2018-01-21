package com.zigzag.whar.ui.dashboard.feeds

import com.zigzag.whar.arch.BaseContract
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider

/**
 * Created by salah on 30/12/17.
 */

class FeedsContract {
    interface View : BaseContract.View {
        fun tempShowLocation(location : String)
        fun displayFeeds()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun initiate(locationProvider : ReactiveLocationProvider)
        fun sendMessage(text : String)
    }

}
