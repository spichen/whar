package com.zigzag.whar.ui.login

import android.util.Log
import com.google.firebase.auth.PhoneAuthCredential
import com.zigzag.whar.arch.BasePresenter
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

    override fun resendCode(phoneNumber : Number) {
        return rxFirebaseAuth.resendCode(phoneNumber,verificationData.token)
    }

    override fun signIn(phoneAuthCredential: PhoneAuthCredential) {
        rxFirebaseAuth
                .signInWithPhoneAuthCredential(phoneAuthCredential)
                .subscribe({
                    Log.d(TAG,"Signed In ")
                }) {
                    throwable: Throwable? ->
                    Log.e(TAG,throwable?.localizedMessage)
                    //view?.showError(R.string.invalid_code)
                }.track()
    }

    private fun requestCode() : ObservableTransformer<RequestCodeEvent,SubmitUiModel> {
        return ObservableTransformer { events ->
            events.flatMap { event ->
                rxFirebaseAuth.phoneAuthProvider(event.number)
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
                                SubmitUiModel.success()
                            else
                                SubmitUiModel.codeSent()
                        }
                        .onErrorReturn { t -> SubmitUiModel.failure(t) }
                        .observeOn(AndroidSchedulers.mainThread())
                        .startWith(SubmitUiModel.inProgress())
            }
        }
    }


    private fun verifyCode(): ObservableTransformer<VerifyCodeEvent, SubmitUiModel> {
        return ObservableTransformer { events ->
            events.flatMap { event ->
                rxFirebaseAuth.signInWithCode(this.verificationData.verificationId, event.code)
                        .map { SubmitUiModel.success() }
                        .onErrorReturn { t -> SubmitUiModel.failure(t) }
                        .observeOn(AndroidSchedulers.mainThread())
                        .startWith(SubmitUiModel.inProgress())
            }
        }
    }

    override fun submitTransformer(): ObservableTransformer<SubmitEvent, SubmitUiModel> {
        return ObservableTransformer { events ->
            events.publish { shared ->
                Observable.merge(
                        shared.ofType(RequestCodeEvent::class.java).compose(requestCode()),
                        shared.ofType(VerifyCodeEvent::class.java).compose(verifyCode())
                )
            }
        }
    }

    abstract class SubmitEvent

    class RequestCodeEvent(var number: Number) : SubmitEvent()
    class VerifyCodeEvent(var code: Number) : SubmitEvent()

    class SubmitUiModel(var inProgress : Boolean,var success : Boolean,var codeSent :  Boolean = false ,var errorMessage : String? = null){
        companion object {
            fun inProgress() = SubmitUiModel(true,false)
            fun codeSent() = SubmitUiModel(false,false,true)
            fun success() = SubmitUiModel(false,true)
            fun failure(t : Throwable) = SubmitUiModel(false,false,false, t.localizedMessage.split(".")[0])
        }
    }
}
