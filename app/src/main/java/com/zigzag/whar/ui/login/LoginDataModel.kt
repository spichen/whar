package com.zigzag.whar.ui.login

import com.zigzag.riverx.RiveRxAction
import com.zigzag.riverx.RiveRxEvent
import com.zigzag.riverx.RiveRxResult
import com.zigzag.riverx.RiveRxViewState
import com.zigzag.whar.rx.firebase.VerificationData

/**
 * Created by salah on 25/1/18.
 */

class LoginDataModel {
    sealed class LoginAction : RiveRxAction {
        object IdleAction : LoginAction()
        data class LoginAttemptAction(val phoneNumber: Number) : LoginAction()
        data class VerifyCodeAction(val code: Number, val verificationId : String?) : LoginAction()
        data class ValidatePhoneNumberAction(val phoneNumber: Number) : LoginAction()
        data class ValidateCodeAction(val code: Number) :  LoginAction()
    }

    sealed class LoginEvent : RiveRxEvent {
        object InitialEvent : LoginEvent()
        data class AttemptLoginEvent(var number: Number) : LoginEvent()
        data class VerifyCodeEvent(var code: Number) : LoginEvent()
        data class ValidatePhoneNumberEvent(var number: Number) : LoginEvent()
        data class ValidateCodeEvent(var code: Number) : LoginEvent()
    }

    sealed class LoginResult : RiveRxResult {
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
    ) : RiveRxViewState {
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