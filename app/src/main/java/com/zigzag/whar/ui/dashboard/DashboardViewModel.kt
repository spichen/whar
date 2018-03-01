package com.zigzag.whar.ui.dashboard

import com.rxfuel.rxfuel.RxFuelAction
import com.rxfuel.rxfuel.RxFuelResult
import com.rxfuel.rxfuel.RxFuelViewModel
import com.zigzag.whar.ui.dashboard.DashboardPresentationModels.*

class DashboardViewModel : RxFuelViewModel<DashboardEvent, DashboardViewState>() {
    override fun eventToAction(event: DashboardEvent): RxFuelAction {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override var initialState: DashboardViewState
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}

    override fun eventToViewState(previousState: DashboardViewState, event: DashboardEvent): DashboardViewState {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resultToViewState(previousState: DashboardViewState, result: RxFuelResult): DashboardViewState {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stateAfterNavigation(navigationState: DashboardViewState): DashboardViewState {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
