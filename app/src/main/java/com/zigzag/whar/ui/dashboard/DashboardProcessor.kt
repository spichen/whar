package com.zigzag.whar.ui.dashboard

import com.salah.rxfuel.RiveRxProcessor
import com.zigzag.whar.rx.firebase.RxFirebaseAuth
import io.reactivex.ObservableTransformer
import javax.inject.Inject
import com.zigzag.whar.ui.dashboard.DashboardPresentationModels.*

/**
 * Created by salah on 26/1/18.
 */

class DashboardProcessor @Inject constructor(): RiveRxProcessor<DashboardAction,DashboardResult>() {

    @Inject
    lateinit var rxFirebaseAuth : RxFirebaseAuth

    override val processors: HashMap<Class<out DashboardAction>, ObservableTransformer<out DashboardAction, out DashboardResult>>
        get() = hashMapOf(

        )
/*

    private fun processDashboardAttempt() =
            ObservableTransformer<DashboardAction.DashboardAttemptAction, DashboardResult.DashboardAttemptResult> { actions ->
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
                                    is Boolean -> DashboardResult.DashboardAttemptResult.Success
                                    is VerificationData -> DashboardResult.DashboardAttemptResult.CodeSent(action.phoneNumber, it)
                                    else -> DashboardResult.DashboardAttemptResult.Failure(Throwable("Unknown Error"))
                                }
                            }
                            .onErrorReturn { t -> DashboardResult.DashboardAttemptResult.Failure(t) }
                            .observeOn(AndroidSchedulers.mainThread())
                            .startWith(DashboardResult.DashboardAttemptResult.InFlight)
                }
            }

    private fun processCodeVerification() =
            ObservableTransformer<DashboardAction.VerifyCodeAction, DashboardResult.VerifyCodeResult>  { actions ->
                actions.flatMap { action ->
                    rxFirebaseAuth.signInWithCode(action.verificationId, action.code!!)
                            .map {
                                if(it) DashboardResult.VerifyCodeResult.Success
                                else DashboardResult.VerifyCodeResult.Failure(Throwable("Unknown Error"))
                            }
                            .onErrorReturn { t -> DashboardResult.VerifyCodeResult.Failure(t) }
                            .observeOn(AndroidSchedulers.mainThread())
                            .startWith(DashboardResult.VerifyCodeResult.InFlight)
                }
            }

    private fun processCodeResend() =
            ObservableTransformer<DashboardAction.ResendCode, DashboardResult.CodeResent>  { actions ->
                actions.flatMap { action ->
                    Observable.just(rxFirebaseAuth.resendCode(action.phoneNumber, action.token))
                            .map { DashboardResult.CodeResent }
                            .observeOn(AndroidSchedulers.mainThread())
                }
            }
*/

}