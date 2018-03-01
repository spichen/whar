package com.zigzag.whar.data.firebaseProcessor

import com.rxfuel.rxfuel.RxFuelResult
import com.zigzag.whar.rx.firebase.VerificationData

sealed class FirebaseResult : RxFuelResult {

    sealed class LoginAttemptResult : FirebaseResult() {
        object InFlight : LoginAttemptResult()
        object Success : LoginAttemptResult()
        data class CodeSent(val phoneNumber : Number,val verificationData: VerificationData) : LoginAttemptResult()
        data class Failure(val error: Throwable) : LoginAttemptResult()
    }

    sealed class VerifyCodeResult : FirebaseResult() {
        object InFlight : VerifyCodeResult()
        object Success : VerifyCodeResult()
        data class Failure(val error: Throwable) : VerifyCodeResult()
    }

    object CodeResent : FirebaseResult()
}