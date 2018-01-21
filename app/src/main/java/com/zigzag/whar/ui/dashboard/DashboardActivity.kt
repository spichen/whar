package com.zigzag.whar.ui.dashboard

import android.Manifest
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Menu
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zigzag.whar.R
import com.zigzag.whar.arch.BaseActivity
import com.zigzag.whar.common.ActivityUtils
import com.zigzag.whar.ui.dashboard.feeds.FeedsFragment
import com.zigzag.whar.ui.dashboard.fragments.PermissionFragment
import kotlinx.android.synthetic.main.activity_dashboard.*
import javax.inject.Inject
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth


class DashboardActivity :  BaseActivity<DashboardContract.View, DashboardContract.Presenter>(), DashboardContract.View {

    @Inject
    lateinit var feedsFragment : FeedsFragment

    @Inject
    lateinit var permissionFragment : PermissionFragment

    @Inject
    lateinit var dashboardPresenter: DashboardPresenter

    override fun getPresenterImpl(): DashboardContract.Presenter = dashboardPresenter

    var fragment : Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        //toolbar.setLogo(R.drawable.logo)

        fragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container)

        if (fragment == null) {
            RxPermissions(this)
                    .request(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                    .subscribe({ granted ->
                        if (granted) {
                            presenter.displayFeedsFragment()
                        } else {
                            presenter.displayPermissionFragment()
                        }
                    })
        }
    }

    override fun displayFeedsFragment() {
        fragment = feedsFragment
        ActivityUtils.addFragmentToActivity(supportFragmentManager, fragment as FeedsFragment, R.id.main_fragment_container)
    }

    override fun displayPermissionFragment() {
        fragment = permissionFragment
        ActivityUtils.addFragmentToActivity(supportFragmentManager, fragment as PermissionFragment, R.id.main_fragment_container)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.item_logout){
            FirebaseAuth.getInstance().signOut()
        }
        return super.onOptionsItemSelected(item)
    }
}
