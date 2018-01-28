package com.zigzag.whar.ui.login

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import com.jakewharton.rxbinding2.widget.textChanges
import com.zigzag.whar.R
import com.zigzag.whar.common.Utils
import kotlinx.android.synthetic.main.activity_login.*
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding2.view.clicks
import android.telephony.TelephonyManager
import com.jakewharton.rxbinding2.widget.editorActions
import io.reactivex.Observable
import android.view.ViewGroup
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.zigzag.whar.common.helpers.ViewHelpers.whiteLongSnackBar
import kotlinx.android.synthetic.main.page_phone_number.view.*
import kotlinx.android.synthetic.main.page_code.view.*
import android.view.inputmethod.InputMethodManager
import com.salah.riverx.RiveRxView
import java.util.*
import com.salah.riverx.RiveRxDelegate
import com.salah.riverx.RiveRxDelegateImpl
import com.zigzag.whar.base.BaseActivity
import com.zigzag.whar.ui.dashboard.DashboardActivity
import com.zigzag.whar.ui.login.LoginDataModel.*
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.observables.ConnectableObservable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LoginActivity :
        BaseActivity(),
        RiveRxView<LoginEvent, LoginViewState>{

    @Inject
    lateinit var viewModel : LoginViewModel

    override val riveRx: RiveRxDelegate
        get() =  RiveRxDelegateImpl(this, viewModel)

    companion object {
        const val PAGE_PHONE_NUMBER = 0
        const val PAGE_CODE = 1
    }

    var phoneNumberPage : View? = null
    var codePage : View? = null

    private lateinit var submitButtonObservable : ConnectableObservable<Unit>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initViews()
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        riveRx.onStart()
    }

    override fun render(state: LoginViewState) {
        animateSubmitButton(state.inProgress)
        enableInputFields(!state.inProgress)
        enableSubmitButton(!state.invalid)
        if(!state.inProgress){
            when {
                state.inputNumber -> displayPhoneNumberInputPage()
                state.codeSent -> {
                    codePage?.tv_phone_number?.text = getString(R.string.tap_to_edit, state.lastPhoneNumber)
                    when(if(state.code == null) 0 else state.code.toString().length){
                        0 -> codePage?.et_code_1?.requestFocus()
                        1 -> codePage?.et_code_2?.requestFocus()
                        2 -> codePage?.et_code_3?.requestFocus()
                        3 -> codePage?.et_code_4?.requestFocus()
                        4 -> codePage?.et_code_5?.requestFocus()
                        5 -> codePage?.et_code_6?.requestFocus()
                    }
                    if(state.codeResent) showSuccess(R.string.code_resent)
                    displayCodeInputPage()
                }
                state.success -> completeSubmitButtonAnimation()
            }

            if(state.errorMessage!=null) showError(state.errorMessage)
        }
    }

    override fun events(): Observable<LoginEvent> {
        return Observable.merge(
                Arrays.asList(
                        attemptLoginEvent(),
                        verifyCodeEvent(),
                        resendCodeEvent()
                )
        )
    }

    override fun localEvents(): Observable<LoginEvent> {
        return Observable.merge(
                Arrays.asList(
                        editPhoneNumberEvent(),
                        validateCodeEvent(),
                        validatePhoneNumberEvent()
                )
        )
    }

    private fun initViews() {
        submitButtonObservable = btn_submit.clicks().publish()

        phoneNumberPage = View.inflate(this,R.layout.page_phone_number,null)
        codePage = View.inflate(this,R.layout.page_code,null)

        vp_container.adapter = PhoneAuthSliderAdapter()

        val spinnerArrayAdapter = ArrayAdapter<String>(this, R.layout.country_spinner_item, resources.getStringArray(R.array.country_code))
        spinnerArrayAdapter.setDropDownViewResource(R.layout.country_spinner_popup_item)

        phoneNumberPage?.s_country_code?.adapter = spinnerArrayAdapter
        phoneNumberPage?.s_country_code?.setSelection(getDefaultCountryPosition())

        submitButtonObservable.connect()
    }

    private fun verifyCodeEvent() =
            Observable.merge(
                    codePage?.et_code_6?.editorActions()?.filter { validateCode() },
                    submitButtonObservable.filter { vp_container.currentItem == PAGE_CODE }
            ).map { LoginEvent.VerifyCodeEvent(getUserInputtedCode()) }

    private fun attemptLoginEvent() =
            Observable.merge(
                    phoneNumberPage?.et_number?.editorActions()?.filter { validateNumber() },
                    submitButtonObservable.filter { vp_container.currentItem == PAGE_PHONE_NUMBER }
            ).map { LoginEvent.AttemptLoginEvent(getUserInputtedNumber()) }


    private fun validateCodeEvent() =
            Observable.merge(
                Arrays.asList(
                    codePage?.et_code_1?.textChanges(),
                    codePage?.et_code_2?.textChanges(),
                    codePage?.et_code_3?.textChanges(),
                    codePage?.et_code_4?.textChanges(),
                    codePage?.et_code_5?.textChanges(),
                    codePage?.et_code_6?.textChanges()
                ))
                .map {
                    LoginEvent.ValidateCodeEvent(getUserInputtedCode())
                }


    private fun validatePhoneNumberEvent() = phoneNumberPage?.et_number?.textChanges()
                ?.map {
                    if(it.isEmpty()) LoginEvent.ValidatePhoneNumberEvent(0)
                    else LoginEvent.ValidatePhoneNumberEvent(it.toString().toLong())
                }

    private fun editPhoneNumberEvent() = codePage?.tv_phone_number?.clicks()
            ?.map{
                LoginEvent.EditNumberEvent
            }

    private fun resendCodeEvent() = tv_resend?.clicks()
            ?.throttleFirst(20000,TimeUnit.MILLISECONDS)
            ?.map{
                LoginEvent.ResendCodeEvent
            }


    private fun animateSubmitButton(animate : Boolean){
        if(animate){
            btn_submit.startAnimation()
        }else{
            revertSubmitButton()
        }
    }

    private fun enableInputFields(enable : Boolean) {
        phoneNumberPage?.et_number?.isEnabled = enable
        phoneNumberPage?.s_country_code?.isEnabled = enable
        codePage?.et_code_1?.isEnabled = enable
        codePage?.et_code_2?.isEnabled = enable
        codePage?.et_code_3?.isEnabled = enable
        codePage?.et_code_4?.isEnabled = enable
        codePage?.et_code_5?.isEnabled = enable
        codePage?.et_code_6?.isEnabled = enable
    }

    private fun revertSubmitButton() {
        if(btn_submit.isAnimating){
            btn_submit.revertAnimation {
                updateSubmitButton()
            }
        }else{
            updateSubmitButton()
        }
    }

    private fun updateSubmitButton(){
        btn_submit.setText(if(vp_container.currentItem == PAGE_PHONE_NUMBER) R.string.get_code else R.string.verify )
    }

    private fun completeSubmitButtonAnimation() {
        // @TODO add success animation
    }


    private fun validateNumber() : Boolean {
        return Utils.isValidMobile(phoneNumberPage?.et_number?.text.toString())
    }

    private fun validateCode() : Boolean{
        return getUserInputtedCode().toString().length == 6
    }


    private fun displayCodeInputPage() {
        vp_container.currentItem = PAGE_CODE
        tv_resend.visibility = VISIBLE
    }

    private fun displayPhoneNumberInputPage() {
        vp_container.currentItem = PAGE_PHONE_NUMBER
        updateSubmitButton()
        tv_resend.visibility = GONE
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

    private fun getUserInputtedCode() : Number? {
        val code = codePage?.et_code_1?.text.toString() +
                codePage?.et_code_2?.text.toString() +
                codePage?.et_code_3?.text.toString() +
                codePage?.et_code_4?.text.toString() +
                codePage?.et_code_5?.text.toString() +
                codePage?.et_code_6?.text.toString()
        return if(code.isEmpty()){
            null
        }else{
            code.toLong()
        }
    }

    private fun getUserInputtedNumber() : Number {
        return (phoneNumberPage?.s_country_code?.selectedItem.toString().split("+")[1] + phoneNumberPage?.et_number?.text.toString().toLong()).toLong()
    }

    private fun showError(error: String?) {
        if(error != null){
            whiteLongSnackBar(btn_submit,error,R.color.colorPrimaryDark)
            revertSubmitButton()
        }
    }

    private fun showError(error: Int) {
        showError(getString(error))
    }

    private fun showSuccess(message: String) {
        whiteLongSnackBar(btn_submit,message,R.color.colorSuccess)
        revertSubmitButton()
    }

    private fun showSuccess(message: Int) {
        showSuccess(getString(message))
    }

    private fun enableSubmitButton(enable: Boolean) {
        btn_submit.isEnabled = enable
        btn_submit.alpha = if(enable) 1f else 0.5f
    }

    internal inner class PhoneAuthSliderAdapter : PagerAdapter() {
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

    override fun onDestroy() {
        super.onDestroy()
        btn_submit.dispose()
    }

}
