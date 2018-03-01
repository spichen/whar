package com.zigzag.whar.ui.dashboard

import android.os.Bundle
import android.util.Log
import com.rxfuel.rxfuel.RxFuel
import com.rxfuel.rxfuel.RxFuelView
import com.zigzag.whar.R
import com.zigzag.whar.base.BaseActivity
import com.zigzag.whar.ui.dashboard.DashboardPresentationModels.*
import io.reactivex.Observable

class DashboardActivity : BaseActivity(), RxFuelView<DashboardEvent, DashboardViewState> {

    private val rxFuel = RxFuel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
    }

    override fun onStart() {
        super.onStart()
        rxFuel.bind(DashboardViewModel())
    }

    override fun events(): Observable<DashboardEvent> {
        return Observable.just("eventts").map { DashboardEvent("event") }
    }

    override fun render(state: DashboardViewState) {
        Log.d("Dashboard -- ",state.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        rxFuel.unbind()
    }
}
