package com.zigzag.whar.ui.login

import com.rxfuel.rxfuel.EventScope
import com.rxfuel.rxfuel.RxFuelEvent
import com.rxfuel.rxfuel.Scope

sealed class LoginEvent : RxFuelEvent {

    @EventScope(Scope.DOMAIN)
    data class AttemptLoginEvent(var number: Number) : LoginEvent()

    @EventScope(Scope.DOMAIN)
    data class VerifyCodeEvent(var code: Number?) : LoginEvent()

    @EventScope(Scope.UI)
    data class ValidatePhoneNumberEvent(var number: Number) : LoginEvent()

    @EventScope(Scope.UI)
    data class ValidateCodeEvent(var code: Number?) : LoginEvent()

    @EventScope(Scope.UI)
    object EditNumberEvent : LoginEvent()

    @EventScope(Scope.DOMAIN)
    object ResendCodeEvent : LoginEvent()
}