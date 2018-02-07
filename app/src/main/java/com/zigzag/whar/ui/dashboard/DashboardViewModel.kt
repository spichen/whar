package com.zigzag.whar.ui.dashboard

import com.salah.rxfuel.RiveRxViewModel
import com.zigzag.whar.common.notOfType
import com.zigzag.whar.rx.firebase.VerificationData
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import com.zigzag.whar.ui.dashboard.DashboardPresentationModels.*
import javax.inject.Inject

/**
 * Created by salah on 23/1/18.
 */

class DashboardViewModel @Inject constructor(dashboardProcessor: DashboardProcessor) :
        RiveRxViewModel<DashboardEvent, DashboardAction, DashboardResult, DashboardViewState>(dashboardProcessor) {

    private var verificationData : VerificationData? = null
    private var code : Number? = null
    private var phoneNumber : Number? = null

    override fun intentFilter() : ObservableTransformer<DashboardEvent, DashboardEvent> =
            ObservableTransformer { intents ->
                intents.publish { shared ->
                    Observable.merge<DashboardEvent>(
                            shared.ofType(DashboardEvent::class.java).take(1),
                            shared.notOfType(DashboardEvent::class.java)
                    )
                }
            }

    override fun eventToAction(event: DashboardEvent): DashboardAction {
        return DashboardAction("action")
        /*return when (event) {
            is DashboardEvent.AttemptDashboardEvent -> {
                phoneNumber = event.number
                DashboardAction.DashboardAttemptAction(event.number)
            }
            is DashboardEvent.VerifyCodeEvent -> DashboardAction.VerifyCodeAction(code, verificationData?.verificationId)
            DashboardDataModel.DashboardEvent.ResendCodeEvent -> DashboardAction.ResendCode(phoneNumber!!, verificationData?.token!!)
            else -> {
                DashboardAction.IdleAction
            }
        }*/
    }

    override fun eventToResult(event: DashboardEvent): DashboardResult {
        return DashboardResult("result")
        /*return when (event) {
            DashboardEvent.EditNumberEvent -> DashboardResult.EditNumber
            is DashboardEvent.ValidatePhoneNumberEvent -> when {
                Utils.isValidMobile(event.number.toString()) -> DashboardResult.ValidatePhoneNumberResult.Valid
                else -> {
                    DashboardResult.ValidatePhoneNumberResult.Invalid
                }
            }
            is DashboardEvent.ValidateCodeEvent ->{
                code = event.code
                when {
                    event.code.toString().length == 6 -> DashboardResult.ValidateCodeResult.Valid
                    else -> {
                        DashboardResult.ValidateCodeResult.Invalid
                    }
                }
            }
            else -> DashboardResult.EditNumber
        }*/
    }

    override var idleState: DashboardViewState
        get() = DashboardViewState("qqqq")
        set(value) {}

    override fun resultToViewState() = BiFunction { previousState: DashboardViewState, result: DashboardResult ->
        return@BiFunction DashboardViewState("Asdasd")
        /*when (result) {
            is DashboardResult.DashboardAttemptResult -> when (result) {
                DashboardResult.DashboardAttemptResult.Success -> previousState.copy(
                        inProgress = false,
                        success = true,
                        errorMessage = null,
                        codeResent = false
                )
                is DashboardResult.DashboardAttemptResult.Failure -> previousState.copy(
                        inProgress = false,
                        success = false,
                        errorMessage = result.error.localizedMessage,
                        codeResent = false
                )
                DashboardResult.DashboardAttemptResult.InFlight -> previousState.copy(
                        inProgress = true,
                        errorMessage = null,
                        codeResent = false
                )
                is DashboardResult.DashboardAttemptResult.CodeSent -> {
                    verificationData = result.verificationData
                    return@BiFunction previousState.copy(
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
            is DashboardResult.VerifyCodeResult -> when (result) {
                DashboardResult.VerifyCodeResult.Success -> previousState.copy(
                        success = true,
                        inProgress = false,
                        errorMessage = null,
                        codeResent = false
                )
                is DashboardResult.VerifyCodeResult.Failure -> previousState.copy(
                        inProgress = false,
                        code = null,
                        errorMessage = result.error.localizedMessage,
                        codeResent = false
                )
                DashboardResult.VerifyCodeResult.InFlight -> previousState.copy(
                        inProgress = true,
                        errorMessage = null,
                        codeResent = false
                )
            }
            is DashboardResult.ValidatePhoneNumberResult -> when (result) {
                DashboardResult.ValidatePhoneNumberResult.Valid -> previousState.copy(
                        invalid = false,
                        errorMessage = null,
                        codeResent = false
                )
                DashboardResult.ValidatePhoneNumberResult.Invalid -> previousState.copy(
                        invalid = true,
                        errorMessage = null,
                        codeResent = false
                )
            }
            is DashboardResult.ValidateCodeResult -> when (result) {
                DashboardResult.ValidateCodeResult.Valid -> previousState.copy(
                        code = code,
                        invalid = false,
                        errorMessage = null,
                        codeResent = false
                )
                DashboardResult.ValidateCodeResult.Invalid -> previousState.copy(
                        code = code,
                        invalid = true,
                        errorMessage = null,
                        codeResent = false
                )
            }
            DashboardDataModel.DashboardResult.EditNumber -> previousState.copy(
                    code = null,
                    inputNumber = true,
                    codeSent = false,
                    invalid = true,
                    errorMessage = null,
                    codeResent = false
            )
            DashboardDataModel.LoginResult.CodeResent -> previousState.copy(
                    codeResent = true,
                    errorMessage = null
            )
        }*/
    }
}
