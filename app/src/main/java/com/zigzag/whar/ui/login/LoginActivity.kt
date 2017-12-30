package com.zigzag.whar.ui.login

import android.os.Bundle
import com.zigzag.whar.R
import com.zigzag.whar.arch.BaseActivity
import javax.inject.Inject

class LoginActivity : BaseActivity<LoginActivityContract.View,LoginActivityContract.Presenter>(), LoginActivityContract.View {

    @Inject lateinit var p : LoginActivityPresenter

    override fun initPresenter(): LoginActivityContract.Presenter = p

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun showLoginError(error: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
