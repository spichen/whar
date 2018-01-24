package com.zigzag.whar.ui.login

import com.zigzag.whar.common.notOfType
import com.zigzag.whar.arch.BaseViewModel
import com.zigzag.whar.rx.firebase.VerificationData
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import com.zigzag.whar.ui.login.LoginDataHolder.*
import javax.inject.Inject

/**
 * Created by salah on 23/1/18.
 */

class LoginViewModel @Inject constructor(val processor: LoginProcessor) : BaseViewModel<LoginIntent, LoginViewState>() {

    private val intentsSubject: PublishSubject<LoginIntent> = PublishSubject.create()
    private val statesObservable: Observable<LoginViewState> = compose()

    override fun processIntents(intents: Observable<LoginIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun states(): Observable<LoginViewState> = statesObservable

    private val intentFilter: ObservableTransformer<LoginIntent, LoginIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge<LoginIntent>(
                        shared.ofType(LoginIntent.InitialIntent::class.java).take(1),
                        shared.notOfType(LoginIntent.InitialIntent::class.java)
                )
            }
        }

    private fun compose(): Observable<LoginViewState> {
        return intentsSubject
                .compose<LoginIntent>(intentFilter)
                .map(this::actionFromIntent)
                .compose(processor.actionProcessor())
                .scan(LoginViewState.idle(), reducer)
                .replay(1)
                .autoConnect(0)
    }


    private fun actionFromIntent(intent: LoginIntent): LoginAction {
        return when (intent) {
            is LoginIntent.AttemptLoginIntent -> LoginAction.LoginAttemptAction(intent.number)
            is LoginIntent.VerifyCodeIntent -> LoginAction.VerifyCodeAction(intent.code, verificationData?.verificationId)
            is LoginIntent.ValidatePhoneNumberIntent -> LoginAction.ValidatePhoneNumberAction(intent.number)
            is LoginIntent.ValidateCodeIntent -> LoginAction.ValidateCodeAction(intent.code)
            else -> {
                LoginAction.IdleAction
            }
        }
    }

    companion object {
        private var verificationData : VerificationData? = null

        private val reducer = BiFunction { previousState: LoginViewState, result: LoginResult ->
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
}
