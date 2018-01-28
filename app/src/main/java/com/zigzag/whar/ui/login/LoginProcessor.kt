package com.zigzag.whar.ui.login

import com.google.firebase.auth.PhoneAuthCredential
import com.salah.riverx.RiveRxProcessor
import com.zigzag.whar.rx.firebase.RxFirebaseAuth
import com.zigzag.whar.rx.firebase.VerificationData
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject
import com.zigzag.whar.ui.login.LoginDataModel.*

/**
 * Created by salah on 26/1/18.
 */

class LoginProcessor @Inject constructor(): RiveRxProcessor<LoginAction,LoginResult>() {

    @Inject
    lateinit var rxFirebaseAuth : RxFirebaseAuth

    override val processors: HashMap<Class<out LoginAction>, ObservableTransformer<out LoginAction, out LoginResult>>
        get() = hashMapOf(
                Pair(LoginAction.LoginAttemptAction::class.java,processLoginAttempt()),
                Pair(LoginAction.VerifyCodeAction::class.java,processCodeVerification()),
                Pair(LoginAction.ResendCode::class.java,processCodeResend())
        )

    private fun processLoginAttempt() =
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

    private fun processCodeVerification() =
            ObservableTransformer<LoginAction.VerifyCodeAction, LoginResult.VerifyCodeResult>  { actions ->
                actions.flatMap { action ->
                    rxFirebaseAuth.signInWithCode(action.verificationId, action.code!!)
                            .map {
                                if(it) LoginResult.VerifyCodeResult.Success
                                else LoginResult.VerifyCodeResult.Failure(Throwable("Unknown Error"))
                            }
                            .onErrorReturn { t -> LoginResult.VerifyCodeResult.Failure(t) }
                            .observeOn(AndroidSchedulers.mainThread())
                            .startWith(LoginResult.VerifyCodeResult.InFlight)
                }
            }

    private fun processCodeResend() =
            ObservableTransformer<LoginAction.ResendCode, LoginResult.CodeResent>  { actions ->
                actions.flatMap { action ->
                    Observable.just(rxFirebaseAuth.resendCode(action.phoneNumber, action.token))
                            .map { LoginResult.CodeResent }
                            .observeOn(AndroidSchedulers.mainThread())
                }
            }

}