package com.zigzag.whar.ui.login

import com.zigzag.whar.mviarch.BaseIntent
import com.zigzag.whar.rx.firebase.VerificationData

/**
 * Created by salah on 23/1/18.
 */
sealed class LoginIntent : BaseIntent {
    object InitialIntent : LoginIntent()
    data class AttemptLoginIntent(var number: Number) : LoginIntent()
    data class VerifyCodeIntent(var code: Number) : LoginIntent()
    data class ValidatePhoneNumberIntent(var number: Number) : LoginIntent()
    data class ValidateCodeIntent(var code: Number) : LoginIntent()
}