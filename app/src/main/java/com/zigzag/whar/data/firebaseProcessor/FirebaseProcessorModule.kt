package com.zigzag.whar.data.firebaseProcessor

import com.google.firebase.auth.PhoneAuthCredential
import com.rxfuel.rxfuel.RxFuelProcessor
import com.rxfuel.rxfuel.RxFuelProcessorModule
import com.zigzag.whar.rx.firebase.RxFirebaseAuth
import com.zigzag.whar.rx.firebase.VerificationData
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class FirebaseProcessorModule @Inject constructor(): RxFuelProcessorModule {

    @Inject
    lateinit var rxFirebaseAuth : RxFirebaseAuth

    @RxFuelProcessor(FirebaseAction.LoginAttemptAction::class)
    private val processLoginAttempt =
            ObservableTransformer<FirebaseAction.LoginAttemptAction, FirebaseResult.LoginAttemptResult> { actions ->
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
                                    is Boolean -> FirebaseResult.LoginAttemptResult.Success
                                    is VerificationData -> FirebaseResult.LoginAttemptResult.CodeSent(action.phoneNumber, it)
                                    else -> FirebaseResult.LoginAttemptResult.Failure(Throwable("Unknown Error"))
                                }
                            }
                            .onErrorReturn { t -> FirebaseResult.LoginAttemptResult.Failure(t) }
                            .observeOn(AndroidSchedulers.mainThread())
                            .startWith(FirebaseResult.LoginAttemptResult.InFlight)
                }
            }

    @RxFuelProcessor(FirebaseAction.VerifyCodeAction::class)
    private val processCodeVerification =
            ObservableTransformer<FirebaseAction.VerifyCodeAction, FirebaseResult.VerifyCodeResult>  { actions ->
                actions.flatMap { action ->
                    rxFirebaseAuth.signInWithCode(action.verificationId, action.code!!)
                            .map {
                                if(it) FirebaseResult.VerifyCodeResult.Success
                                else FirebaseResult.VerifyCodeResult.Failure(Throwable("Unknown Error"))
                            }
                            .onErrorReturn { t -> FirebaseResult.VerifyCodeResult.Failure(t) }
                            .observeOn(AndroidSchedulers.mainThread())
                            .startWith(FirebaseResult.VerifyCodeResult.InFlight)
                }
            }

    @RxFuelProcessor(FirebaseAction.ResendCode::class)
    private val processCodeResend =
            ObservableTransformer<FirebaseAction.ResendCode, FirebaseResult.CodeResent>  { actions ->
                actions.flatMap { action ->
                    Observable.just(rxFirebaseAuth.resendCode(action.phoneNumber, action.token))
                            .map { FirebaseResult.CodeResent }
                            .observeOn(AndroidSchedulers.mainThread())
                }
            }

}