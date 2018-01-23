package com.zigzag.whar.ui.login

import com.zigzag.whar.mviarch.BaseViewState

/**
 * Created by salah on 23/1/18.
 */

data class LoginViewState(
        var invalid : Boolean,
        var inProgress : Boolean,
        var success : Boolean,
        var codeSent :  Boolean = false,
        var errorMessage : String? = null,
        var lastPhoneNumber : Number? = null
) : BaseViewState {
    companion object {
        fun idle(): LoginViewState {
            return LoginViewState(
                    invalid = false,
                    inProgress = false,
                    success = false,
                    codeSent = false,
                    errorMessage = null)
        }
    }
}