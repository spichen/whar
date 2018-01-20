package com.zigzag.whar.ui.dashboard

import android.Manifest
import android.os.Bundle
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zigzag.whar.R
import com.zigzag.whar.arch.BaseActivity
import com.zigzag.whar.common.ActivityUtils
import com.zigzag.whar.ui.dashboard.feeds.FeedsFragment
import javax.inject.Inject

class DashboardActivity :  BaseActivity<DashboardContract.View, DashboardContract.Presenter>(), DashboardContract.View {
    @Inject
    lateinit var feedsFragment : FeedsFragment

    @Inject
    lateinit var dashboardPresenter: DashboardPresenter

    override fun getPresenterImpl(): DashboardContract.Presenter = dashboardPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        var fragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container)
        if (fragment == null) {
            RxPermissions(this)
                    .request(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                    .subscribe({ granted ->
                        if (granted) {
                            fragment = feedsFragment
                            ActivityUtils.addFragmentToActivity(supportFragmentManager, fragment, R.id.main_fragment_container)
                        } else {

                        }
                    })
        }
    }

}
