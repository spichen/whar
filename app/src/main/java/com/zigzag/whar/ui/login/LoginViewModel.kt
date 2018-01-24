package com.zigzag.whar.ui.login

import com.google.firebase.auth.PhoneAuthCredential
import com.zigzag.whar.common.notOfType
import com.zigzag.whar.arch.BaseViewModel
import com.zigzag.whar.common.Utils
import com.zigzag.whar.rx.firebase.RxFirebaseAuth
import com.zigzag.whar.rx.firebase.VerificationData
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import com.zigzag.whar.ui.login.LoginModels.*

/**
 * Created by salah on 23/1/18.
 */

class LoginViewModel @Inject constructor() : BaseViewModel<LoginIntent, LoginViewState>() {

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
                .compose(actionProcessor)
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

    @Inject
    lateinit var rxFirebaseAuth : RxFirebaseAuth

    private val processLoginAttempt =
            ObservableTransformer<LoginAction.LoginAttemptAction, LoginResult.LoginAttemptResult> { actions ->
                actions.flatMap { action ->
                    rxFirebaseAuth.phoneAuthProvider(action.phoneNumber)
                            .compose {
                                it.flatMap {
                                    when (it) {
                                        is PhoneAuthCredential -> rxFirebaseAuth.signInWithPhoneAuthCredential(it)
                                        is VerificationData -> Observable.just(it)
                                        else -> Observable.error(Throwable("Unknown Error"))
                                    }
                                }
                            }
                            .map {
                                when (it) {
                                    is Boolean -> LoginResult.LoginAttemptResult.Success
                                    is VerificationData -> LoginResult.LoginAttemptResult.CodeSent(action.phoneNumber, it)
                                    else -> LoginResult.LoginAttemptResult.Failure(Throwable("Unknown Error"))
                                }
                            }
                            .onErrorReturn { t -> LoginResult.LoginAttemptResult.Failure(t) }
                            .observeOn(AndroidSchedulers.mainThread())
                            .startWith(LoginResult.LoginAttemptResult.InFlight)
                }
            }

    private val processCodeVerification =
            ObservableTransformer<LoginAction.VerifyCodeAction, LoginResult.VerifyCodeResult>  { actions ->
                actions.flatMap { action ->
                    rxFirebaseAuth.signInWithCode(action.verificationId, action.code)
                            .map {
                                if(it) LoginResult.VerifyCodeResult.Success
                                else LoginResult.VerifyCodeResult.Failure(Throwable("Unknown Error"))
                            }
                            .onErrorReturn { t -> LoginResult.VerifyCodeResult.Failure(t) }
                            .observeOn(AndroidSchedulers.mainThread())
                            .startWith(LoginResult.VerifyCodeResult.InFlight)
                }
            }

    private val processCodeValidation = ObservableTransformer<LoginAction.ValidateCodeAction, LoginResult.ValidateCodeResult> { actions ->
        actions.flatMap { action ->
            Observable.just(action.code.toString().length == 6)
                    .map { if(it) LoginResult.ValidateCodeResult.Valid else LoginResult.ValidateCodeResult.Invalid}
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    private val processPhoneNumberValidation = ObservableTransformer<LoginAction.ValidatePhoneNumberAction, LoginResult.ValidatePhoneNumberResult> { actions ->
        actions.flatMap { action ->
            Observable.just(Utils.isValidMobile(action.phoneNumber.toString()))
                    .map { if (it) LoginResult.ValidatePhoneNumberResult.Valid else LoginResult.ValidatePhoneNumberResult.Invalid }
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    internal var actionProcessor =
            ObservableTransformer<LoginAction, LoginResult> { actions ->
                actions.publish({ shared ->
                    Observable.merge<LoginResult>(
                            shared.ofType(LoginAction.LoginAttemptAction::class.java).compose(processLoginAttempt),
                            shared.ofType(LoginAction.VerifyCodeAction::class.java).compose(processCodeVerification),
                            shared.ofType(LoginAction.ValidatePhoneNumberAction::class.java).compose(processPhoneNumberValidation),
                            shared.ofType(LoginAction.ValidateCodeAction::class.java).compose(processCodeValidation)
                    )
                })
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
