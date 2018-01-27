package com.zigzag.whar.ui.dashboard

import android.Manifest
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

class DashboardActivity : AppCompatActivity()/*:  RiveRxActivity<DashboardContract.View, DashboardContract.Presenter>(), DashboardContract.View*/ {
/*
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
    }*/
}
