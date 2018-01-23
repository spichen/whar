package com.zigzag.whar.ui.login

import com.zigzag.whar.mviarch.BaseResult
import com.zigzag.whar.rx.firebase.VerificationData

/**
 * Created by salah on 23/1/18.
 */

sealed class LoginResult : BaseResult {
    sealed class LoginAttemptResult : LoginResult() {
        object InFlight : LoginAttemptResult()
        object Success : LoginAttemptResult()
        data class CodeSent(val phoneNumber : Number,val verificationData: VerificationData) : LoginAttemptResult()
        data class Failure(val error: Throwable) : LoginAttemptResult()
    }

    sealed class VerifyCodeResult : LoginResult() {
        object InFlight : VerifyCodeResult()
        object Success : VerifyCodeResult()
        data class Failure(val error: Throwable) : VerifyCodeResult()
    }

    sealed class ValidatePhoneNumberResult : LoginResult() {
        object Valid : ValidatePhoneNumberResult()
        object Invalid : ValidatePhoneNumberResult()
    }

    sealed class ValidateCodeResult : LoginResult() {
        object Valid : ValidateCodeResult()
        object Invalid : ValidateCodeResult()
    }
}