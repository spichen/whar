package com.zigzag.whar.ui.login

import com.zigzag.riverx.RiveRxViewModel
import com.zigzag.whar.common.notOfType
import com.zigzag.whar.rx.firebase.VerificationData
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import com.zigzag.whar.ui.login.LoginDataModel.*
import javax.inject.Inject

/**
 * Created by salah on 23/1/18.
 */

class LoginViewModel @Inject constructor(loginProcessor : LoginProcessor) : RiveRxViewModel<LoginEvent, LoginAction, LoginResult, LoginViewState>(loginProcessor) {

    private var verificationData : VerificationData? = null

    override fun intentFilter() : ObservableTransformer<LoginEvent, LoginEvent> =
            ObservableTransformer { intents ->
                intents.publish { shared ->
                    Observable.merge<LoginEvent>(
                            shared.ofType(LoginEvent.InitialEvent::class.java).take(1),
                            shared.notOfType(LoginEvent.InitialEvent::class.java)
                    )
                }
            }

    override fun eventToAction(event: LoginEvent): LoginAction {
        return when (event) {
            is LoginEvent.AttemptLoginEvent -> LoginAction.LoginAttemptAction(event.number)
            is LoginEvent.VerifyCodeEvent -> LoginAction.VerifyCodeAction(event.code, verificationData?.verificationId)
            is LoginEvent.ValidatePhoneNumberEvent -> LoginAction.ValidatePhoneNumberAction(event.number)
            is LoginEvent.ValidateCodeEvent -> LoginAction.ValidateCodeAction(event.code)
            else -> {
                LoginAction.IdleAction
            }
        }
    }

    override var idleState: LoginViewState
        get() = LoginViewState.idle()
        set(value) {}

    override fun resultToViewState() = BiFunction { previousState: LoginViewState, result: LoginResult ->
        when (result) {
            is LoginResult.LoginAttemptResult -> when (result) {
                is LoginResult.LoginAttemptResult.Success -> previousState.copy(
                        inProgress = false,
                        success = true,
                        errorMessage = null
                )
                is LoginResult.LoginAttemptResult.Failure -> previousState.copy(
                        inProgress = false,
                        success = false,
                        errorMessage = result.error.localizedMessage
                )
                is LoginResult.LoginAttemptResult.InFlight -> previousState.copy(
                        inProgress = true
                )
                is LoginResult.LoginAttemptResult.CodeSent -> {
                    verificationData = result.verificationData
                    return@BiFunction previousState.copy(
                            inProgress = false,
                            codeSent = true,
                            lastPhoneNumber = result.phoneNumber
                    )
                }
            }

            is LoginResult.VerifyCodeResult -> when (result) {
                is LoginResult.VerifyCodeResult.Success -> previousState.copy(
                        success = true,
                        inProgress = false
                )
                is LoginResult.VerifyCodeResult.Failure -> previousState.copy(
                        inProgress = false,
                        codeSent = false,
                        errorMessage = result.error.localizedMessage
                )
                is LoginResult.VerifyCodeResult.InFlight -> previousState.copy(
                        inProgress = true
                )
            }
            is LoginResult.ValidatePhoneNumberResult -> when (result) {
                is LoginResult.ValidatePhoneNumberResult.Valid -> previousState.copy(
                        invalid = false
                )
                is LoginResult.ValidatePhoneNumberResult.Invalid -> previousState.copy(
                        invalid = true
                )
            }
            is LoginResult.ValidateCodeResult -> when (result) {
                is LoginResult.ValidateCodeResult.Valid -> previousState.copy(
                        invalid = false
                )
                is LoginResult.ValidateCodeResult.Invalid -> previousState.copy(
                        invalid = true
                )
            }
        }
    }
}
