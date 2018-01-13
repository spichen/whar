package com.zigzag.whar.ui.dashboard

import android.os.Bundle
import com.zigzag.whar.R
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.view.clicks
import com.zigzag.whar.arch.BaseActivity
import kotlinx.android.synthetic.main.activity_dashboard.*
import javax.inject.Inject

class DashboardActivity : BaseActivity<DashboardActivityContract.View, DashboardActivityContract.Presenter>(), DashboardActivityContract.View{

    @Inject lateinit var dashboardActivityPresenter : DashboardActivityPresenter

    override fun getPresenterImpl(): DashboardActivityContract.Presenter = dashboardActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        btn_test.clicks()
                .subscribe {
                    FirebaseAuth.getInstance().signOut()
                }
    }

    override fun gotoDashboard() {
        //NO-OP
    }
}
