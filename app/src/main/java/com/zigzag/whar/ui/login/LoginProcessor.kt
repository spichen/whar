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
            ObservableTransformer<LoginDataModel.LoginAction.LoginAttemptAction, LoginDataModel.LoginResult.LoginAttemptResult> { actions ->
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
                                    is Boolean -> LoginDataModel.LoginResult.LoginAttemptResult.Success
                                    is VerificationData -> LoginDataModel.LoginResult.LoginAttemptResult.CodeSent(action.phoneNumber, it)
                                    else -> LoginDataModel.LoginResult.LoginAttemptResult.Failure(Throwable("Unknown Error"))
                                }
                            }
                            .onErrorReturn { t -> LoginDataModel.LoginResult.LoginAttemptResult.Failure(t) }
                            .observeOn(AndroidSchedulers.mainThread())
                            .startWith(LoginDataModel.LoginResult.LoginAttemptResult.InFlight)
                }
            }

    private val processCodeVerification =
            ObservableTransformer<LoginDataModel.LoginAction.VerifyCodeAction, LoginDataModel.LoginResult.VerifyCodeResult>  { actions ->
                actions.flatMap { action ->
                    rxFirebaseAuth.signInWithCode(action.verificationId, action.code)
                            .map {
                                if(it) LoginDataModel.LoginResult.VerifyCodeResult.Success
                                else LoginDataModel.LoginResult.VerifyCodeResult.Failure(Throwable("Unknown Error"))
                            }
                            .onErrorReturn { t -> LoginDataModel.LoginResult.VerifyCodeResult.Failure(t) }
                            .observeOn(AndroidSchedulers.mainThread())
                            .startWith(LoginDataModel.LoginResult.VerifyCodeResult.InFlight)
                }
            }

    private val processCodeValidation = ObservableTransformer<LoginDataModel.LoginAction.ValidateCodeAction, LoginDataModel.LoginResult.ValidateCodeResult> { actions ->
        actions.flatMap { action ->
            Observable.just(action.code.toString().length == 6)
                    .map { if(it) LoginDataModel.LoginResult.ValidateCodeResult.Valid else LoginDataModel.LoginResult.ValidateCodeResult.Invalid}
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    private val processPhoneNumberValidation = ObservableTransformer<LoginDataModel.LoginAction.ValidatePhoneNumberAction, LoginDataModel.LoginResult.ValidatePhoneNumberResult> { actions ->
        actions.flatMap { action ->
            Observable.just(Utils.isValidMobile(action.phoneNumber.toString()))
                    .map { if (it) LoginDataModel.LoginResult.ValidatePhoneNumberResult.Valid else LoginDataModel.LoginResult.ValidatePhoneNumberResult.Invalid }
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun actionProcessor() =
            ObservableTransformer<LoginDataModel.LoginAction, LoginDataModel.LoginResult> { actions ->
                actions.publish({ shared ->
                    Observable.merge<LoginDataModel.LoginResult>(
                            shared.ofType(LoginDataModel.LoginAction.LoginAttemptAction::class.java).compose(processLoginAttempt),
                            shared.ofType(LoginDataModel.LoginAction.VerifyCodeAction::class.java).compose(processCodeVerification),
                            shared.ofType(LoginDataModel.LoginAction.ValidatePhoneNumberAction::class.java).compose(processPhoneNumberValidation),
                            shared.ofType(LoginDataModel.LoginAction.ValidateCodeAction::class.java).compose(processCodeValidation)
                    )
                })
            }
}