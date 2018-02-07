package com.zigzag.whar.ui.dashboard

import com.salah.rxfuel.RiveRxAction
import com.salah.rxfuel.RiveRxEvent
import com.salah.rxfuel.RiveRxResult
import com.salah.rxfuel.RiveRxViewState

/**
 * Created by salah on 29/1/18.
 */
class DashboardPresentationModels {
    data class DashboardEvent(val text : String) : RiveRxEvent
    data class DashboardAction(val text : String) : RiveRxAction
    data class DashboardResult(val text : String) : RiveRxResult
    data class DashboardViewState(val text : String) : RiveRxViewState
}