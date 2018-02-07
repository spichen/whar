package com.zigzag.whar.ui.dashboard

import android.os.Bundle
import android.util.Log
import com.salah.rxfuel.RiveRx
import com.salah.rxfuel.RiveRxView
import com.zigzag.whar.R
import com.zigzag.whar.base.BaseActivity
import com.zigzag.whar.ui.dashboard.DashboardPresentationModels.*
import io.reactivex.Observable
import javax.inject.Inject

class DashboardActivity : BaseActivity(), RiveRxView<DashboardEvent, DashboardViewState> {

    @Inject
    lateinit var viewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
    }

    override fun onStart() {
        super.onStart()
        RiveRx.observeView(this,viewModel)
    }

    override fun localEvents(): Observable<DashboardEvent> {
        return Observable.just("eventts").map { DashboardEvent("localEvents") }
    }

    override fun events(): Observable<DashboardEvent> {
        return Observable.just("eventts").map { DashboardEvent("event") }
    }

    override fun render(state: DashboardViewState) {
        Log.d("Dashboard -- ",state.toString())
    }
}
