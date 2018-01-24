package com.zigzag.whar.ui.login

import com.google.firebase.auth.PhoneAuthCredential
import com.zigzag.whar.common.Utils
import com.zigzag.whar.rx.firebase.RxFirebaseAuth
import com.zigzag.whar.rx.firebase.VerificationData
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * Created by salah on 25/1/18.
 */

class LoginProcessor @Inject constructor() {

    @Inject
    lateinit var rxFirebaseAuth : RxFirebaseAuth

    private val processLoginAttempt =
            ObservableTransformer<LoginDataHolder.LoginAction.LoginAttemptAction, LoginDataHolder.LoginResult.LoginAttemptResult> { actions ->
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
                                    is Boolean -> LoginDataHolder.LoginResult.LoginAttemptResult.Success
                                    is VerificationData -> LoginDataHolder.LoginResult.LoginAttemptResult.CodeSent(action.phoneNumber, it)
                                    else -> LoginDataHolder.LoginResult.LoginAttemptResult.Failure(Throwable("Unknown Error"))
                                }
                            }
                            .onErrorReturn { t -> LoginDataHolder.LoginResult.LoginAttemptResult.Failure(t) }
                            .observeOn(AndroidSchedulers.mainThread())
                            .startWith(LoginDataHolder.LoginResult.LoginAttemptResult.InFlight)
                }
            }

    private val processCodeVerification =
            ObservableTransformer<LoginDataHolder.LoginAction.VerifyCodeAction, LoginDataHolder.LoginResult.VerifyCodeResult>  { actions ->
                actions.flatMap { action ->
                    rxFirebaseAuth.signInWithCode(action.verificationId, action.code)
                            .map {
                                if(it) LoginDataHolder.LoginResult.VerifyCodeResult.Success
                                else LoginDataHolder.LoginResult.VerifyCodeResult.Failure(Throwable("Unknown Error"))
                            }
                            .onErrorReturn { t -> LoginDataHolder.LoginResult.VerifyCodeResult.Failure(t) }
                            .observeOn(AndroidSchedulers.mainThread())
                            .startWith(LoginDataHolder.LoginResult.VerifyCodeResult.InFlight)
                }
            }

    private val processCodeValidation = ObservableTransformer<LoginDataHolder.LoginAction.ValidateCodeAction, LoginDataHolder.LoginResult.ValidateCodeResult> { actions ->
        actions.flatMap { action ->
            Observable.just(action.code.toString().length == 6)
                    .map { if(it) LoginDataHolder.LoginResult.ValidateCodeResult.Valid else LoginDataHolder.LoginResult.ValidateCodeResult.Invalid}
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    private val processPhoneNumberValidation = ObservableTransformer<LoginDataHolder.LoginAction.ValidatePhoneNumberAction, LoginDataHolder.LoginResult.ValidatePhoneNumberResult> { actions ->
        actions.flatMap { action ->
            Observable.just(Utils.isValidMobile(action.phoneNumber.toString()))
                    .map { if (it) LoginDataHolder.LoginResult.ValidatePhoneNumberResult.Valid else LoginDataHolder.LoginResult.ValidatePhoneNumberResult.Invalid }
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun actionProcessor() =
            ObservableTransformer<LoginDataHolder.LoginAction, LoginDataHolder.LoginResult> { actions ->
                actions.publish({ shared ->
                    Observable.merge<LoginDataHolder.LoginResult>(
                            shared.ofType(LoginDataHolder.LoginAction.LoginAttemptAction::class.java).compose(processLoginAttempt),
                            shared.ofType(LoginDataHolder.LoginAction.VerifyCodeAction::class.java).compose(processCodeVerification),
                            shared.ofType(LoginDataHolder.LoginAction.ValidatePhoneNumberAction::class.java).compose(processPhoneNumberValidation),
                            shared.ofType(LoginDataHolder.LoginAction.ValidateCodeAction::class.java).compose(processCodeValidation)
                    )
                })
            }
}