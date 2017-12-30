package com.zigzag.whar.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import com.jakewharton.rxbinding2.InitialValueObservable
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import com.zigzag.whar.R
import com.zigzag.whar.arch.BaseActivity
import com.zigzag.whar.common.Utils
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.design.snackbar
import javax.inject.Inject

class LoginActivity : BaseActivity<LoginActivityContract.View,LoginActivityContract.Presenter>(), LoginActivityContract.View {

    @Inject lateinit var p : LoginActivityPresenter

    override fun initPresenter(): LoginActivityContract.Presenter = LoginActivityPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        hideCodeInput()
        initObservation()
    }

    private fun initObservation() {
        et_number.textChanges()
                .subscribe { query ->
                    if(Utils.isValidMobile(query)){
                        enableSubmitButton()
                    }else {
                        disableSubmitButton()
                    }
                }

        btn_submit.clicks()
                .subscribe {
                    displayCodeInput()
                }

    }

    override fun showError(error: String) {
        snackbar(btn_submit,error)
    }

    override fun displayCodeInput() {
        et_code.visibility = VISIBLE
    }

    override fun hideCodeInput() {
        et_code.visibility = GONE
    }

    override fun setSubmitButtonText(text: String) {
        btn_submit.text = text
    }

    override fun disableSubmitButton() {
        btn_submit.isEnabled = false
    }

    override fun enableSubmitButton() {
        btn_submit.isEnabled = true
    }

}
