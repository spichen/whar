package com.zigzag.whar.ui.login

import com.zigzag.whar.mviarch.BaseAction
import com.zigzag.whar.rx.firebase.VerificationData

/**
 * Created by salah on 23/1/18.
 */

sealed class LoginAction : BaseAction {
    object IdleAction : LoginAction()
    data class LoginAttemptAction(val phoneNumber: Number) : LoginAction()
    data class VerifyCodeAction(val code: Number, val verificationId : String?) : LoginAction()
    data class ValidatePhoneNumberAction(val phoneNumber: Number) : LoginAction()
    data class ValidateCodeAction(val code: Number) : LoginAction()
}