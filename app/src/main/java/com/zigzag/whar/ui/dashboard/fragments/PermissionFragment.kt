package com.zigzag.whar.ui.dashboard.fragments

import android.Manifest
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zigzag.whar.R
import com.zigzag.whar.di.ActivityScoped
import kotlinx.android.synthetic.main.fragment_permission.*
import javax.inject.Inject

@ActivityScoped
class PermissionFragment @Inject constructor() : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_permission, container, false)
    }
/*

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
*/
/*
        btn_request_permission.clicks()
                .compose(RxPermissions(activity).ensure(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
                .subscribe { granted ->
                    if(granted)

                }*//*


    }
*/

}