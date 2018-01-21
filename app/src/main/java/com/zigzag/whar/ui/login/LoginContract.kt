package com.zigzag.whar.ui.login

import com.google.firebase.auth.PhoneAuthCredential
import com.zigzag.whar.arch.BaseContract
import io.reactivex.ObservableTransformer

/**
 * Created by salah on 30/12/17.
 */

class LoginContract {
    interface View : BaseContract.View {
        fun showSuccess(message: String)
        fun showSuccess(message: Int)
        fun setSubmitButtonText(text: String)
        fun displayCodeInputPage()
        fun displayPhoneNumberInputPage()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun submitTransformer() : ObservableTransformer<LoginPresenter.SubmitEvent, LoginPresenter.SubmitUiModel>
        fun resendCode(phoneNumber : Number)
        fun signIn(phoneAuthCredential : PhoneAuthCredential)
    }
}
