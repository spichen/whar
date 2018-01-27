package com.zigzag.whar.ui.login

import android.util.Log
import com.zigzag.riverx.RiveRxViewModel
import com.zigzag.whar.common.Utils
import com.zigzag.whar.common.notOfType
import com.zigzag.whar.di.ActivityScoped
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
    private var code : Number? = null

    init {
        Log.d("RiveRx","login view model ini")
    }

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
            is LoginEvent.VerifyCodeEvent -> LoginAction.VerifyCodeAction(code, verificationData?.verificationId)
            else -> {
                LoginAction.IdleAction
            }
        }
    }

    override fun eventToResult(event: LoginEvent): LoginResult {
        return when (event) {
            LoginEvent.EditNumberEvent -> LoginResult.EditNumber
            is LoginEvent.ValidatePhoneNumberEvent -> when {
                Utils.isValidMobile(event.number.toString()) -> LoginResult.ValidatePhoneNumberResult.Valid
                else -> {
                    LoginResult.ValidatePhoneNumberResult.Invalid
                }
            }
            is LoginEvent.ValidateCodeEvent ->{
                code = event.code
                when {
                    event.code.toString().length == 6 -> LoginResult.ValidateCodeResult.Valid
                    else -> {
                        LoginResult.ValidateCodeResult.Invalid
                    }
                }
            }
            else -> LoginResult.EditNumber
        }
    }


    override var idleState: LoginViewState
        get() = LoginViewState.idle()
        set(value) {}

    override fun resultToViewState() = BiFunction { previousState: LoginViewState, result: LoginResult ->

        Log.d("RiveRx - previous state",previousState.toString())
        Log.d("RiveRx - result",result.toString())
        Log.d("RiveRx -","-----------------------------------")

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
                        inProgress = true,
                        errorMessage = null
                )
                is LoginResult.LoginAttemptResult.CodeSent -> {
                    verificationData = result.verificationData
                    return@BiFunction previousState.copy(
                            invalid = true,
                            inProgress = false,
                            inputNumber = false,
                            codeSent = true,
                            lastPhoneNumber = result.phoneNumber,
                            errorMessage = null
                    )
                }
            }

            is LoginResult.VerifyCodeResult -> when (result) {
                is LoginResult.VerifyCodeResult.Success -> previousState.copy(
                        success = true,
                        inProgress = false,
                        errorMessage = null

                )
                is LoginResult.VerifyCodeResult.Failure -> previousState.copy(
                        inProgress = false,
                        code = null,
                        errorMessage = result.error.localizedMessage
                )
                is LoginResult.VerifyCodeResult.InFlight -> previousState.copy(
                        inProgress = true,
                        errorMessage = null
                )
            }
            is LoginResult.ValidatePhoneNumberResult -> when (result) {
                is LoginResult.ValidatePhoneNumberResult.Valid -> previousState.copy(
                        invalid = false,
                        errorMessage = null
                )
                is LoginResult.ValidatePhoneNumberResult.Invalid -> previousState.copy(
                        invalid = true,
                        errorMessage = null
                )
            }
            is LoginResult.ValidateCodeResult -> when (result) {
                is LoginResult.ValidateCodeResult.Valid -> previousState.copy(
                        code = code,
                        invalid = false,
                        errorMessage = null
                )
                is LoginResult.ValidateCodeResult.Invalid -> previousState.copy(
                        code = code,
                        invalid = true,
                        errorMessage = null
                )
            }
            LoginDataModel.LoginResult.EditNumber -> previousState.copy(
                    code = null,
                    inputNumber = true,
                    codeSent = false,
                    errorMessage = null
            )
        }
    }
}
