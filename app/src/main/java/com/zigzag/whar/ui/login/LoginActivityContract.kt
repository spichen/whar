package com.zigzag.whar.ui.login

import com.zigzag.whar.arch.BaseContract

/**
 * Created by salah on 30/12/17.
 */

class LoginActivityContract {
    interface View : BaseContract.View {
        fun showError(error: String)
        fun showSuccess(message: String)
        fun setSubmitButtonText(text: String)
        fun disableSubmitButton()
        fun enableSubmitButton()
        fun updateUItoInputCode()
        fun updateUItoInputNumber()
        fun onVerified()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun requestCode(number: Number)
        fun verifyCode(code: Number)
        fun resendCode()
    }
}
