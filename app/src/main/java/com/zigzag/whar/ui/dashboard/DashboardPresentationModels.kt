package com.zigzag.whar.ui.dashboard

import android.support.v4.app.FragmentActivity
import com.rxfuel.rxfuel.RxFuelEvent
import com.rxfuel.rxfuel.RxFuelViewState
import kotlin.reflect.KClass


class DashboardPresentationModels {
    data class DashboardEvent(val text : String) : RxFuelEvent
    data class DashboardViewState(val text : String, override var navigate: KClass<out FragmentActivity>?) : RxFuelViewState
}