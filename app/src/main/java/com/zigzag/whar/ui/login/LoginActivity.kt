package com.zigzag.whar.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.jakewharton.rxbinding2.widget.textChanges
import com.zigzag.whar.R
import com.zigzag.whar.arch.BaseActivity
import com.zigzag.whar.common.Utils
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding2.view.clicks
import android.telephony.TelephonyManager
import com.jakewharton.rxbinding2.widget.editorActions
import io.reactivex.Observable
import android.view.ViewGroup
import android.support.v4.view.PagerAdapter
import android.view.View
import com.zigzag.whar.ui.dashboard.DashboardActivity
import com.zigzag.whar.common.helpers.ViewHelpers.whiteLongSnackBar
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.page_phone_number.view.*
import kotlinx.android.synthetic.main.page_code.view.*
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager


class LoginActivity : BaseActivity<LoginActivityContract.View,LoginActivityContract.Presenter>(), LoginActivityContract.View{

    @Inject lateinit var p : LoginActivityPresenter

    val TAG = "LoginActivity"
    val PAGE_PHONE_NUMBER = 0
    val PAGE_CODE = 1
    var phoneNumberPage : View? = null
    var codePage : View? = null

    override fun initPresenter(): LoginActivityContract.Presenter = p

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initViews()
        initObservation()
    }

    private fun initViews() {
        phoneNumberPage = View.inflate(this,R.layout.page_phone_number,null)
        codePage = View.inflate(this,R.layout.page_code,null)

        vp_container.adapter = phoneAuthSliderAdapter()

        val spinnerArrayAdapter = ArrayAdapter<String>(this, R.layout.country_spinner_item, resources.getStringArray(R.array.country_code))
        phoneNumberPage?.s_country_code?.adapter = spinnerArrayAdapter
        spinnerArrayAdapter.setDropDownViewResource(R.layout.country_spinner_popup_item)
        phoneNumberPage?.s_country_code?.setSelection(getDefaultCountryPosition())
    }

    private fun initObservation() {
        phoneNumberPage?.et_number?.textChanges()
                ?.subscribe { query ->
                    if(Utils.isValidMobile(query)){
                        enableSubmitButton()
                    }else {
                        disableSubmitButton()
                    }
                }
        Observable.merge(phoneNumberPage?.et_number?.editorActions(), btn_submit.clicks())
                .observeOn(AndroidSchedulers.mainThread())
                .map{phoneNumberPage?.s_country_code?.selectedItem.toString().split("+")[1] + phoneNumberPage?.et_number?.text.toString()}
                .subscribe { query ->
                    if(vp_container.currentItem == PAGE_PHONE_NUMBER){
                        hideKeyboard()
                        try {
                            presenter.requestCode(query.toLong())
                            btn_submit.startAnimation()
                        } catch (e : Exception){
                            phoneNumberPage?.et_number?.error = getString(R.string.invalid_phone_number)
                        }
                    }else{
                        presenter.verifyCode(getUserInputtedCode())
                    }
                }
        codePage?.tv_phone_number?.clicks()
                ?.subscribe{
                    updateUItoInputNumber()
                }

        codePage?.et_code_1?.textChanges()
                ?.subscribe{ query ->
                    validateVerifyButton()
                    if(query.length == 1){
                        codePage?.et_code_2?.requestFocus()
                    }
                }
        codePage?.et_code_2?.textChanges()
                ?.subscribe{ query ->
                    validateVerifyButton()
                    if(query.length == 1){
                        codePage?.et_code_3?.requestFocus()
                    }
                }
        codePage?.et_code_3?.textChanges()
                ?.subscribe{ query ->
                    validateVerifyButton()
                    if(query.length == 1){
                        codePage?.et_code_4?.requestFocus()
                    }
                }
        codePage?.et_code_4?.textChanges()
                ?.subscribe{ query ->
                    validateVerifyButton()
                    if(query.length == 1){
                        codePage?.et_code_5?.requestFocus()
                    }
                }
        codePage?.et_code_5?.textChanges()
                ?.subscribe{ query ->
                    validateVerifyButton()
                    if(query.length == 1){
                        codePage?.et_code_6?.requestFocus()
                    }
                }
        codePage?.et_code_6?.textChanges()
                ?.subscribe{
                    validateVerifyButton()
                }
    }

    private fun validateVerifyButton(){
        if(getUserInputtedCode().toString().length == 6){
            enableSubmitButton()
        }else{
            disableSubmitButton()
        }
    }

    internal inner class phoneAuthSliderAdapter : PagerAdapter() {
        override fun instantiateItem(collection: ViewGroup, position: Int): View? {
            when (position) {
                PAGE_PHONE_NUMBER -> {
                    collection.addView(phoneNumberPage)
                    return phoneNumberPage
                }
                PAGE_CODE -> {
                    collection.addView(codePage)
                    return codePage
                }
            }
            return phoneNumberPage
        }

        override fun getCount(): Int {
            return 2
        }

        override fun isViewFromObject(arg0: View?, arg1: Any): Boolean {
            return arg0 === arg1
        }
    }

    override fun updateUItoInputCode() {
        btn_submit.revertAnimation{
            btn_submit.background = ContextCompat.getDrawable(this,R.drawable.login_button_shape)
            btn_submit.setText(R.string.verify)
        }
        disableSubmitButton()

        codePage?.tv_phone_number?.text = getString(R.string.tap_to_edit,phoneNumberPage?.et_number?.text.toString())
        vp_container.currentItem = PAGE_CODE

        codePage?.et_code_1?.requestFocus()

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(codePage?.et_code_1, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun updateUItoInputNumber() {
        btn_submit.background = ContextCompat.getDrawable(this,R.drawable.login_button_shape)
        btn_submit.setText(R.string.get_code)
        vp_container.currentItem = PAGE_PHONE_NUMBER
    }

    override fun onVerified() {
        startActivity(Intent(this@LoginActivity,DashboardActivity::class.java))
    }

    private fun getDefaultCountryPosition() : Int {
        val manager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        val countryID = manager.simCountryIso.toUpperCase()
        val rl = this.resources.getStringArray(R.array.country_code)
        for (i in rl.indices) {
            val g = rl[i].split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (g[0].trim { it <= ' ' } == countryID.trim { it <= ' ' }) {
                return i
            }
        }
        return 0
    }

    private fun getUserInputtedCode() : Number {
        val code = codePage?.et_code_1?.text.toString() +
                codePage?.et_code_2?.text.toString() +
                codePage?.et_code_3?.text.toString() +
                codePage?.et_code_4?.text.toString() +
                codePage?.et_code_5?.text.toString() +
                codePage?.et_code_6?.text.toString()
        return if(code.isEmpty()){
            0
        }else{
            code.toLong()
        }
    }

    override fun showError(error: String) {
        whiteLongSnackBar(btn_submit,error)
        btn_submit.revertAnimation()
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
    override fun onDestroy() {
        super.onDestroy()
        btn_submit.dispose()
    }
}
