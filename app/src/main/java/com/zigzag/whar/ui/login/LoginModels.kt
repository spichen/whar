package com.zigzag.whar.ui.login

import com.zigzag.whar.arch.BaseAction
import com.zigzag.whar.arch.BaseIntent
import com.zigzag.whar.arch.BaseResult
import com.zigzag.whar.arch.BaseViewState
import com.zigzag.whar.rx.firebase.VerificationData

/**
 * Created by salah on 25/1/18.
 */
sealed class LoginModels {
    sealed class LoginAction : BaseAction {
        object IdleAction : LoginAction()
        data class LoginAttemptAction(val phoneNumber: Number) : LoginAction()
        data class VerifyCodeAction(val code: Number, val verificationId : String?) : LoginAction()
        data class ValidatePhoneNumberAction(val phoneNumber: Number) : LoginAction()
        data class ValidateCodeAction(val code: Number) :  LoginAction()
    }

    sealed class LoginIntent : BaseIntent {
        object InitialIntent : LoginIntent()
        data class AttemptLoginIntent(var number: Number) : LoginIntent()
        data class VerifyCodeIntent(var code: Number) : LoginIntent()
        data class ValidatePhoneNumberIntent(var number: Number) : LoginIntent()
        data class ValidateCodeIntent(var code: Number) : LoginIntent()
    }


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

    data class LoginViewState(
            var invalid : Boolean,
            var inProgress : Boolean,
            var success : Boolean,
            var codeSent :  Boolean = false,
            var errorMessage : String? = null,
            var lastPhoneNumber : Number? = null
    ) : BaseViewState {
        companion object {
            fun idle(): LoginViewState {
                return LoginViewState(
                        invalid = false,
                        inProgress = false,
                        success = false,
                        codeSent = false,
                        errorMessage = null)
            }
        }
    }
}