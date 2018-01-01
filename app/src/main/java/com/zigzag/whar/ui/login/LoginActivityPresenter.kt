package com.zigzag.whar.ui.login

import android.util.Log
import com.google.firebase.auth.PhoneAuthProvider
import com.zigzag.whar.arch.BasePresenter
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential



/**
 * Created by salah on 30/12/17.
 */

class LoginActivityPresenter @Inject constructor() : BasePresenter<LoginActivityContract.View>(), LoginActivityContract.Presenter  {
    
    val TAG = "LoginActivityPresenter";
    
    override fun onPresenterCreated() {
        //view?.disableSubmitButton()
    }

    override fun requestCode(number: Number) {
        Log.d("num",number.toString())
        val mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential)

                //signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            override fun onCodeSent(verificationId: String?,
                                    token: PhoneAuthProvider.ForceResendingToken?) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId!!)

                // Save verification ID and resending token so we can use them later
                //mVerificationId = verificationId
                //mResendToken = token

                // ...
            }
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number.toString(),
                60,
                TimeUnit.SECONDS,
                view as LoginActivity,
                mCallbacks)
    }


}
