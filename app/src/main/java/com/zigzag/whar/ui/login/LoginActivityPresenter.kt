package com.zigzag.whar.ui.login

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.zigzag.whar.R
import com.zigzag.whar.arch.BasePresenter
import com.zigzag.whar.rx.firebase.VerificationData
import javax.inject.Inject

/**
 * Created by salah on 30/12/17.
 */

class LoginActivityPresenter @Inject constructor() : BasePresenter<LoginActivityContract.View>(), LoginActivityContract.Presenter  {

    val TAG = "LoginActivityPresenter"

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
                }
    }

    override fun resendCode() {
        rxFirebaseAuth.resendCode(phoneNumber,verificationData.token)
        view?.showSuccess("Code Resent")
    }

    override fun verifyCode(code: Number) {
        rxFirebaseAuth.signInWithCode(verificationData.verificationId, code.toString())
                    .subscribe({
                        result: FirebaseUser? ->
                        Log.d(TAG,"susss")
                        //view?.onVerified()
                    }) {
                        throwable: Throwable? ->
                        Log.d(TAG,"susss " + throwable?.localizedMessage)
                        view?.showError(R.string.invalid_code)
                    }
    }

    override fun signIn(phoneAuthCredential: PhoneAuthCredential) {
        rxFirebaseAuth
                .signInWithPhoneAuthCredential(phoneAuthCredential)
                .subscribe({
                    result: FirebaseUser? ->
                    Log.d(TAG,"susss")
                    //view?.onVerified()
                }) {
                    throwable: Throwable? ->
                    Log.d(TAG,"susss " + throwable?.localizedMessage)
                    view?.showError(R.string.invalid_code)
                }
    }
}
