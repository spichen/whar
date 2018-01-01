package com.zigzag.whar.ui.login

import android.os.Bundle
import android.util.Log
import com.jakewharton.rxbinding2.widget.textChanges
import com.zigzag.whar.R
import com.zigzag.whar.arch.BaseActivity
import com.zigzag.whar.common.Utils
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.design.snackbar
import javax.inject.Inject
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding2.view.clicks
import android.telephony.TelephonyManager
import com.jakewharton.rxbinding2.widget.editorActions
import io.reactivex.Observable

class LoginActivity : BaseActivity<LoginActivityContract.View,LoginActivityContract.Presenter>(), LoginActivityContract.View{

    @Inject lateinit var p : LoginActivityPresenter

    override fun initPresenter(): LoginActivityContract.Presenter = LoginActivityPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initViews()
        initObservation()
    }

    private fun initViews() {
        val spinnerArrayAdapter = ArrayAdapter<String>(this, R.layout.country_spinner_item, resources.getStringArray(R.array.country_code))
        s_country_code.adapter = spinnerArrayAdapter
        spinnerArrayAdapter.setDropDownViewResource(R.layout.country_spinner_popup_item)
        setDefaultCountryCode()
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
        Observable.merge<Unit>(et_number.editorActions()
                .map{_->Unit}, btn_submit.clicks()).map{_->(s_country_code.selectedItem.toString().split("+")[1] + et_number.text.toString()).toLong()}
                .subscribe { query ->
                    presenter.requestCode(query)
                }
    }

    override fun setDefaultCountryCode() {
        val manager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        val countryID = manager.simCountryIso.toUpperCase()
        val rl = this.resources.getStringArray(R.array.country_code)
        for (i in rl.indices) {
            val g = rl[i].split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (g[0].trim { it <= ' ' } == countryID.trim { it <= ' ' }) {
                s_country_code.setSelection(i)
                break
            }
        }
    }

    override fun showError(error: String) {
        snackbar(btn_submit,error)
    }

    override fun setSubmitButtonText(text: String) {
        btn_submit.text = text
    }

    override fun disableSubmitButton() {
        btn_submit.isEnabled = false
        btn_submit.alpha = 0.5f
    }

    override fun enableSubmitButton() {
        btn_submit.isEnabled = true
        btn_submit.alpha = 1f
    }

}
