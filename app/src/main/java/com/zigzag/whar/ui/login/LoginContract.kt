package com.zigzag.whar.ui.login

import com.google.firebase.auth.PhoneAuthCredential
import com.zigzag.whar.arch.BaseContract

/**
 * Created by salah on 30/12/17.
 */

class LoginContract {
    interface View : BaseContract.View {
        fun showError(error: String)
        fun showError(error: Int)
        fun showSuccess(message: String)
        fun showSuccess(message: Int)
        fun setSubmitButtonText(text: String)
        fun disableSubmitButton()
        fun enableSubmitButton()
        fun revertSubmitButton()
        fun startSubmitButtonAnimation()
        fun completeSubmitButtonAnimation()
        fun displayCodeInputPage()
        fun displayPhoneNumberInputPage()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun requestCode(number: Number)
        fun verifyCode(code: Number)
        fun resendCode()
        fun signIn(phoneAuthCredential : PhoneAuthCredential)
    }
}
