package com.zigzag.whar.ui.login

import com.google.firebase.auth.PhoneAuthCredential
import com.zigzag.whar.common.Utils
import com.zigzag.whar.rx.firebase.RxFirebaseAuth
import com.zigzag.whar.rx.firebase.VerificationData
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import com.zigzag.whar.ui.login.LoginResult.LoginAttemptResult
import com.zigzag.whar.ui.login.LoginResult.VerifyCodeResult
import com.zigzag.whar.ui.login.LoginResult.ValidateCodeResult
import com.zigzag.whar.ui.login.LoginResult.ValidatePhoneNumberResult
import javax.inject.Inject

/**
 * Created by salah on 23/1/18.
 */

class LoginActionProcessorHolder @Inject constructor() {

    @Inject
    lateinit var rxFirebaseAuth : RxFirebaseAuth

    private val processLoginAttempt =
        ObservableTransformer<LoginAction.LoginAttemptAction, LoginAttemptResult> { actions ->
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
                            is Boolean -> LoginAttemptResult.Success
                            is VerificationData -> LoginAttemptResult.CodeSent(action.phoneNumber, it)
                            else -> LoginAttemptResult.Failure(Throwable("Unknown Error"))
                        }
                    }
                    .onErrorReturn { t -> LoginAttemptResult.Failure(t) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWith(LoginAttemptResult.InFlight)
            }
        }

    private val processCodeVerification =
        ObservableTransformer<LoginAction.VerifyCodeAction, VerifyCodeResult>  { actions ->
            actions.flatMap { action ->
                rxFirebaseAuth.signInWithCode(action.verificationId, action.code)
                    .map {
                        if(it) LoginResult.VerifyCodeResult.Success
                        else LoginResult.VerifyCodeResult.Failure(Throwable("Unknown Error"))
                    }
                    .onErrorReturn { t -> VerifyCodeResult.Failure(t) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWith(LoginResult.VerifyCodeResult.InFlight)
            }
        }

    private val processCodeValidation = ObservableTransformer<LoginAction.ValidateCodeAction, ValidateCodeResult> { actions ->
        actions.flatMap { action ->
                Observable.just(action.code.toString().length == 6)
                        .map { if(it) ValidateCodeResult.Valid else ValidateCodeResult.Invalid}
                        .observeOn(AndroidSchedulers.mainThread())
            }
        }

    private val processPhoneNumberValidation = ObservableTransformer<LoginAction.ValidatePhoneNumberAction, ValidatePhoneNumberResult> { actions ->
            actions.flatMap { action ->
                Observable.just(Utils.isValidMobile(action.phoneNumber.toString()))
                        .map { if (it) ValidatePhoneNumberResult.Valid else ValidatePhoneNumberResult.Invalid }
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

}