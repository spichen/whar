package com.zigzag.whar.ui.login

import android.util.Log
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.zigzag.whar.arch.BasePresenter
import com.zigzag.whar.rx.firebase.RxFirebaseAuth
import com.zigzag.whar.rx.firebase.VerificationData
import javax.inject.Inject

/**
 * Created by salah on 30/12/17.
 */

class LoginActivityPresenter @Inject constructor() : BasePresenter<LoginActivityContract.View>(), LoginActivityContract.Presenter  {
    
    val TAG = "LoginActivityPresenter"

    @Inject
    lateinit var rxFirebaseAuth : RxFirebaseAuth

    override fun onPresenterCreated() {
        //view?.disableSubmitButton()
    }

    override fun requestCode(number: Number) {
        Log.d("num",number.toString())
        rxFirebaseAuth
                .phoneAuthProvider(number)
                .subscribe({
                    result: Any? ->
                        if(result is PhoneAuthCredential){
                            Log.d(TAG, "ccc " + "PhoneAuthCredential")
                            view?.onVerified()
                        }else if (result is VerificationData){
                            Log.d(TAG, "ccc " + result.verificationId)
                            view?.updateUItoInputCode()
                        }
                }) {
                    throwable: Throwable? ->  Log.d(TAG,throwable?.localizedMessage)
                }
    }
}
