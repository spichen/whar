package com.zigzag.whar.ui.login

import com.rxfuel.rxfuel.RxFuelAction
import com.rxfuel.rxfuel.RxFuelResult
import com.rxfuel.rxfuel.RxFuelViewModel
import com.zigzag.whar.common.Utils
import com.zigzag.whar.data.firebaseProcessor.FirebaseAction
import com.zigzag.whar.data.firebaseProcessor.FirebaseResult
import com.zigzag.whar.rx.firebase.VerificationData

class LoginViewModel : RxFuelViewModel<LoginEvent, LoginViewState>() {

    private var verificationData : VerificationData? = null
    private var code : Number? = null
    private var phoneNumber : Number? = null

    override var initialState: LoginViewState
        get() = LoginViewState.idle()
        set(value) {}

    override fun eventToAction(event: LoginEvent) : RxFuelAction {
        return when (event) {
            is LoginEvent.AttemptLoginEvent -> {
                phoneNumber = event.number
                FirebaseAction.LoginAttemptAction(event.number)
            }
            is LoginEvent.VerifyCodeEvent -> FirebaseAction.VerifyCodeAction(code, verificationData?.verificationId)

            LoginEvent.ResendCodeEvent -> FirebaseAction.ResendCode(phoneNumber!!, verificationData?.token!!)

            else -> {
                RxFuelAction.NO_ACTION
            }
        }
    }

    override fun eventToViewState(previousState: LoginViewState, event: LoginEvent): LoginViewState {
        return when(event) {
            is LoginEvent.ValidatePhoneNumberEvent -> when {
                Utils.isValidMobile(event.number.toString()) -> previousState.copy(
                        invalid = false,
                        errorMessage = null,
                        codeResent = false
                )
                else -> previousState.copy(
                        invalid = true,
                        errorMessage = null,
                        codeResent = false
                )
            }

            is LoginEvent.ValidateCodeEvent ->{
                code = event.code
                when {
                    event.code.toString().length == 6 ->  previousState.copy(
                            code = code,
                            invalid = false,
                            errorMessage = null,
                            codeResent = false
                    )
                    else -> previousState.copy(
                            code = code,
                            invalid = true,
                            errorMessage = null,
                            codeResent = false
                    )
                }
            }

            LoginEvent.EditNumberEvent -> previousState.copy(
                    code = null,
                    inputNumber = true,
                    codeSent = false,
                    invalid = true,
                    errorMessage = null,
                    codeResent = false
            )

            else -> LoginViewState.idle()
        }
    }

    override fun resultToViewState(previousState: LoginViewState, result: RxFuelResult): LoginViewState {
        return when (result) {
            is FirebaseResult.LoginAttemptResult -> when (result) {
                FirebaseResult.LoginAttemptResult.Success -> previousState.copy(
                        inProgress = false,
                        success = true,
                        errorMessage = null,
                        codeResent = false
                )
                is FirebaseResult.LoginAttemptResult.Failure -> previousState.copy(
                        inProgress = false,
                        success = false,
                        errorMessage = result.error.localizedMessage,
                        codeResent = false
                )
                FirebaseResult.LoginAttemptResult.InFlight -> previousState.copy(
                        inProgress = true,
                        errorMessage = null,
                        codeResent = false
                )
                is FirebaseResult.LoginAttemptResult.CodeSent -> {
                    verificationData = result.verificationData
                    previousState.copy(
                            invalid = true,
                            inProgress = false,
                            inputNumber = false,
                            codeSent = true,
                            lastPhoneNumber = result.phoneNumber,
                            errorMessage = null,
                            codeResent = false
                    )
                }
            }
            is FirebaseResult.VerifyCodeResult -> when (result) {
                FirebaseResult.VerifyCodeResult.Success -> previousState.copy(
                        success = true,
                        inProgress = false,
                        errorMessage = null,
                        codeResent = false
                )
                is FirebaseResult.VerifyCodeResult.Failure -> previousState.copy(
                        inProgress = false,
                        code = null,
                        errorMessage = result.error.localizedMessage,
                        codeResent = false
                )
                FirebaseResult.VerifyCodeResult.InFlight -> previousState.copy(
                        inProgress = true,
                        errorMessage = null,
                        codeResent = false
                )
            }

            FirebaseResult.CodeResent -> previousState.copy(
                    codeResent = true,
                    errorMessage = null
            )

            else -> LoginViewState.idle()
        }
    }

    override fun stateAfterNavigation(navigationState: LoginViewState): LoginViewState {
        return navigationState.copy(navigate = null, inProgress = false)
    }

}
