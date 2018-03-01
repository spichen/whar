package com.zigzag.whar.ui.login

import android.support.v4.app.FragmentActivity
import com.rxfuel.rxfuel.RxFuelViewState
import kotlin.reflect.KClass

data class LoginViewState(
        var invalid : Boolean,
        var inProgress : Boolean,
        var success : Boolean,
        var inputNumber : Boolean = true,
        var codeSent :  Boolean = false,
        var codeResent :  Boolean = false,
        var code : Number? = null,
        var errorMessage : String? = null,
        var lastPhoneNumber : Number? = null, override var navigate: KClass<out FragmentActivity>? = null
) : RxFuelViewState {
    companion object {
        fun idle(): LoginViewState {
            return LoginViewState(
                    invalid = false,
                    inProgress = false,
                    success = false,
                    inputNumber = true,
                    codeSent = false,
                    codeResent = false,
                    code = null,
                    errorMessage = null)
        }
    }
}