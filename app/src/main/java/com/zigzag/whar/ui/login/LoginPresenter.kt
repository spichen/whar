package com.zigzag.whar.ui.login

import android.util.Log
import com.google.firebase.auth.PhoneAuthCredential
import com.zigzag.whar.arch.BasePresenter
import com.zigzag.whar.common.Utils
import com.zigzag.whar.rx.firebase.VerificationData
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * Created by salah on 30/12/17.
 */

class LoginPresenter @Inject constructor() : BasePresenter<LoginContract.View>(), LoginContract.Presenter  {

    companion object {
        const val TAG = "LoginPresenter"
    }

    private lateinit var verificationData : VerificationData
    private lateinit var phoneNumber: Number

    private val initialState = SubmitUiModel.idle()

    override lateinit var uiObservable : Observable<SubmitUiModel>

    override fun resendCode(phoneNumber : Number) {
        return rxFirebaseAuth.resendCode(phoneNumber,verificationData.token)
    }

    override fun initUiObservable() {
        uiObservable = Observable.merge(view?.getCodeRequestEvent(), view?.getVerifyCodeEvent(), view?.getValidateCodeEvent(), view?.getValidatePhoneNumberEvent())
                .compose(submitTransformer())
                .scan(initialState , { _, result ->
                    if(result == ValidatePhoneNumberResult.INVALID || result == ValidateCodeResult.INVALID){
                        return@scan SubmitUiModel.invalid()
                    }
                    if(result == ValidatePhoneNumberResult.VALID || result == ValidateCodeResult.VALID){
                        return@scan SubmitUiModel.idle()
                    }
                    if(result == RequestCodeResult.IN_FLIGHT || result == VerifyCodeResult.IN_FLIGHT) {
                        return@scan SubmitUiModel.inProgress()
                    }
                    if(result == RequestCodeResult.SUCCESS || result == VerifyCodeResult.SUCCESS) {
                        return@scan SubmitUiModel.success()
                    }
                    if(result == RequestCodeResult.CODE_SENT) {
                        return@scan SubmitUiModel.codeSent(this.phoneNumber)
                    }
                    if(result.failure){
                        return@scan SubmitUiModel.failure(result.errorMessage)
                    }
                    throw IllegalArgumentException("Unknown Error "+result)
                })
    }

    private fun requestCode() : ObservableTransformer<RequestCodeEvent,RequestCodeResult> {
        return ObservableTransformer { actions ->
            actions.flatMap { action ->
                this.phoneNumber = action.number
                rxFirebaseAuth.phoneAuthProvider(action.number)
                        .compose {
                            it.flatMap {
                                when (it) {
                                    is PhoneAuthCredential -> rxFirebaseAuth.signInWithPhoneAuthCredential(it)
                                    is VerificationData -> {
                                        this.verificationData = it
                                        Observable.just(false)
                                    }
                                    else -> Observable.just(false)
                                }
                            }
                        }
                        .map {
                            if(it)
                                RequestCodeResult.SUCCESS
                            else
                                RequestCodeResult.CODE_SENT
                        }
                        .onErrorReturn { t -> RequestCodeResult.failure(t.localizedMessage) }
                        .observeOn(AndroidSchedulers.mainThread())
                        .startWith(RequestCodeResult.IN_FLIGHT)
            }
        }
    }

    private fun verifyCode(): ObservableTransformer<VerifyCodeEvent, VerifyCodeResult> {
        return ObservableTransformer { events ->
            events.flatMap { event ->
                rxFirebaseAuth.signInWithCode(this.verificationData.verificationId, event.code)
                        .map { VerifyCodeResult.SUCCESS }
                        .onErrorReturn { t -> VerifyCodeResult.failure(t.localizedMessage) }
                        .observeOn(AndroidSchedulers.mainThread())
                        .startWith(VerifyCodeResult.IN_FLIGHT)
            }
        }
    }

    private fun validateCode(): ObservableTransformer<ValidateCodeEvent, ValidateCodeResult> {
        return ObservableTransformer { events ->
            events.flatMap { event ->
                Observable.just(event.code.toString().length == 6)
                        .map { if(it) ValidateCodeResult.VALID else ValidateCodeResult.INVALID}
                        .observeOn(AndroidSchedulers.mainThread())
            }
        }
    }

    private fun validateNumber(): ObservableTransformer<ValidatePhoneNumberEvent, ValidatePhoneNumberResult> {
        return ObservableTransformer { events ->
            events.flatMap { event ->
                Observable.just(Utils.isValidMobile(event.number))
                        .map { if (it) ValidatePhoneNumberResult.VALID else ValidatePhoneNumberResult.INVALID }
                        .observeOn(AndroidSchedulers.mainThread())
            }
        }
    }

    private fun submitTransformer(): ObservableTransformer<SubmitEvent, Results> {
        return ObservableTransformer { events ->
            events.publish { shared ->
                Observable.merge(
                        shared.ofType(RequestCodeEvent::class.java).compose(requestCode()),
                        shared.ofType(VerifyCodeEvent::class.java).compose(verifyCode()),
                        shared.ofType(ValidateCodeEvent::class.java).compose(validateCode()),
                        shared.ofType(ValidatePhoneNumberEvent::class.java).compose(validateNumber())
                )
            }
        }
    }

    override fun onPresenterCreated() {
        super.onPresenterCreated()
        Log.d(TAG,"preseneeter createdddd")
    }
}
