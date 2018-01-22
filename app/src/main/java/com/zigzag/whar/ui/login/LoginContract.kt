package com.zigzag.whar.ui.login

import com.zigzag.whar.arch.BaseContract
import io.reactivex.Observable
import io.reactivex.observables.ConnectableObservable

/**
 * Created by salah on 30/12/17.
 */

class LoginContract {
    interface View : BaseContract.View {
        fun showSuccess(message: String)
        fun showSuccess(message: Int)
        fun showError(message: String?)
        fun showError(message: Int)
        fun setSubmitButtonText(text: String)
        fun completeSubmitButtonAnimation()
        fun displayCodeInputPage()
        fun displayPhoneNumberInputPage()

        fun animateSubmitButton(animate : Boolean)
        fun enableInputFields(enable : Boolean)


        fun getCodeRequestEvent() : Observable<RequestCodeEvent>?
        fun getVerifyCodeEvent() : Observable<VerifyCodeEvent>?
        fun getValidatePhoneNumberEvent() : Observable<ValidatePhoneNumberEvent>?
        fun getValidateCodeEvent() : Observable<ValidateCodeEvent>?

    }

    interface Presenter : BaseContract.Presenter<View> {
        val uiObservable : Observable<SubmitUiModel>
        fun initUiObservable()
        fun resendCode(phoneNumber : Number)
    }
}
