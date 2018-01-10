package com.zigzag.whar.ui.login

import android.util.Log
import com.google.firebase.auth.PhoneAuthCredential
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
                            view?.onVerified()
                        }else if (result is VerificationData){
                            view?.updateUItoInputCode()
                        }
                }) {
                    throwable: Throwable? ->
                        view?.showError(throwable!!.localizedMessage.split(".")[0])
                }
    }

    override fun verifyCode(code: Number) {

    }
}
