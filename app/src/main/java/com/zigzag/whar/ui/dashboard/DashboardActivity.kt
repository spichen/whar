package com.zigzag.whar.ui.dashboard

import android.Manifest
import android.os.Bundle
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zigzag.whar.R
import com.zigzag.whar.common.ActivityUtils
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class DashboardActivity : DaggerAppCompatActivity(){

    @Inject
    lateinit var dashboardFragmentProvider: DashboardFragment

    @Inject
    lateinit var dashboardPresenter: DashboardPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        dashboardPresenter.onPresenterCreated()

        var fragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container)
        if (fragment == null) {
            RxPermissions(this)
                    .request(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                    .subscribe({ granted ->
                        if (granted) {
                            fragment = dashboardFragmentProvider
                            ActivityUtils.addFragmentToActivity(supportFragmentManager, fragment, R.id.main_fragment_container)
                        } else {

                        }
                    })
        }
    }
}
