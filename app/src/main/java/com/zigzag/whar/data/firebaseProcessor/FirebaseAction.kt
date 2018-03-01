package com.zigzag.whar.data.firebaseProcessor

import com.google.firebase.auth.PhoneAuthProvider
import com.rxfuel.rxfuel.RxFuelAction

sealed class FirebaseAction : RxFuelAction {

    data class LoginAttemptAction(val phoneNumber: Number) : FirebaseAction()

    data class VerifyCodeAction(val code: Number?, val verificationId : String?) : FirebaseAction()

    data class ResendCode(val phoneNumber : Number, val token: PhoneAuthProvider.ForceResendingToken) : FirebaseAction()
}