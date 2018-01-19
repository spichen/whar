package com.zigzag.whar.ui.login

import android.util.Log
import com.google.firebase.auth.PhoneAuthCredential
import com.zigzag.whar.R
import com.zigzag.whar.arch.BasePresenter
import com.zigzag.whar.rx.firebase.VerificationData
import javax.inject.Inject

/**
 * Created by salah on 30/12/17.
 */

class LoginPresenter @Inject constructor() : BasePresenter<LoginContract.View>(), LoginContract.Presenter  {

    val TAG = "LoginPresenter"

    lateinit var verificationData : VerificationData

    lateinit var phoneNumber : Number

    override fun requestCode(number: Number) {
        phoneNumber = number
        rxFirebaseAuth
                .phoneAuthProvider(number)
                .subscribe({
                    result: Any? ->
                        if(result is PhoneAuthCredential){
                            signIn(result)
                        }else if (result is VerificationData){
                            verificationData = result
                            view?.displayCodeInputPage()
                        }
                }) {
                    throwable: Throwable? ->
                        view?.showError(throwable!!.localizedMessage.split(".")[0])
                }.track()
    }

    override fun resendCode() {
        rxFirebaseAuth.resendCode(phoneNumber,verificationData.token)
        view?.showSuccess("Code Resent")
    }

    override fun verifyCode(code: Number) {
        rxFirebaseAuth
                .signInWithCode(verificationData.verificationId, code.toString())
                .subscribe({
                    Log.d(TAG,"Code verified")
                }) {
                    view?.showError(R.string.invalid_code)
                }.track()
    }

    override fun signIn(phoneAuthCredential: PhoneAuthCredential) {
        rxFirebaseAuth
                .signInWithPhoneAuthCredential(phoneAuthCredential)
                .subscribe({
                    Log.d(TAG,"Signed In ")
                }) {
                    throwable: Throwable? ->
                    Log.e(TAG,throwable?.localizedMessage)
                    view?.showError(R.string.invalid_code)
                }.track()
    }
}
