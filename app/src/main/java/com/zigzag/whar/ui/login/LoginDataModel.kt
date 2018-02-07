package com.zigzag.whar.ui.login

import android.support.v4.app.FragmentActivity
import com.google.firebase.auth.PhoneAuthProvider
import com.rxfuel.rxfuel.RxFuelAction
import com.rxfuel.rxfuel.RxFuelEvent
import com.rxfuel.rxfuel.RxFuelResult
import com.rxfuel.rxfuel.RxFuelViewState
import com.zigzag.whar.rx.firebase.VerificationData
import kotlin.reflect.KClass

/**
 * Created by salah on 25/1/18.
 */

class LoginDataModel {
    sealed class LoginAction : RxFuelAction {
        object IdleAction : LoginAction()
        data class LoginAttemptAction(val phoneNumber: Number) : LoginAction()
        data class VerifyCodeAction(val code: Number?, val verificationId : String?) : LoginAction()
        data class ResendCode(val phoneNumber : Number, val token: PhoneAuthProvider.ForceResendingToken) : LoginAction()
    }

    sealed class LoginEvent : RxFuelEvent {
        object InitialEvent : LoginEvent() {
            override val isLocal: Boolean
                get() = true
        }

        data class AttemptLoginEvent(var number: Number, override val isLocal: Boolean = false) : LoginEvent()
        data class VerifyCodeEvent(var code: Number?, override val isLocal: Boolean = false) : LoginEvent()
        data class ValidatePhoneNumberEvent(var number: Number, override val isLocal: Boolean = true) : LoginEvent()
        data class ValidateCodeEvent(var code: Number?, override val isLocal: Boolean = true) : LoginEvent()
        object EditNumberEvent : LoginEvent() {
            override val isLocal: Boolean
                get() = true
        }

        object ResendCodeEvent : LoginEvent() {
            override val isLocal: Boolean
                get() = false
        }

    }

    sealed class LoginResult : RxFuelResult {

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

        sealed class ValidateCodeResult: LoginResult() {
            object Valid: ValidateCodeResult()
            object Invalid : ValidateCodeResult()
        }

        object EditNumber : LoginResult()
        object CodeResent : LoginResult()
    }

    data class LoginViewState(
            var invalid : Boolean,
            var inProgress : Boolean,
            var success : Boolean,
            var inputNumber : Boolean = true,
            var codeSent :  Boolean = false,
            var codeResent :  Boolean = false,
            var code : Number? = null,
            var errorMessage : String? = null,
            var lastPhoneNumber : Number? = null, override var navigate: KClass<out FragmentActivity>? = null
    ) : RxFuelViewState {
        companion object {
            fun idle(): LoginViewState {
                return LoginViewState(
                        invalid = false,
                        inProgress = false,
                        success = false,
                        inputNumber = true,
                        codeSent = false,
                        codeResent = false,
                        code = null,
                        errorMessage = null)
            }
        }
    }
}