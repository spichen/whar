package com.zigzag.whar.ui.login

/**
 * Created by salah on 23/1/18.
 */

abstract class Results(val failure : Boolean = false, var errorMessage : String? = null) {
    constructor() : this(false,null)
}
abstract class SubmitEvent

class RequestCodeResult(val inFlight : Boolean, val success: Boolean, val codeSent: Boolean, failure: Boolean = false, errorMessage: String? = null) : Results(failure,errorMessage) {
    companion object {
        val IN_FLIGHT = RequestCodeResult(true,false,false)
        val SUCCESS = RequestCodeResult(false,true,false)
        val CODE_SENT = RequestCodeResult(false,false,true)
        fun failure(errorMessage: String?): RequestCodeResult {
            return RequestCodeResult(false,false,false,true,errorMessage)
        }
    }
}
class VerifyCodeResult(val inFlight : Boolean, val success: Boolean, failure: Boolean = false, errorMessage: String? = null) : Results(failure,errorMessage) {
    companion object {
        val IN_FLIGHT = VerifyCodeResult(true,false)
        val SUCCESS = VerifyCodeResult(false,true)
        fun failure(errorMessage: String?): VerifyCodeResult {
            return VerifyCodeResult(false,false,true,errorMessage)
        }
    }
}

class ValidatePhoneNumberResult(val isValid : Boolean) : Results() {
    companion object {
        val VALID = ValidatePhoneNumberResult(true)
        val INVALID = ValidatePhoneNumberResult(false)
    }
}
class ValidateCodeResult(val isValid : Boolean) : Results(){
    companion object {
        val VALID = ValidateCodeResult(true)
        val INVALID = ValidateCodeResult(false)
    }
}

class RequestCodeEvent(var number: Number) : SubmitEvent()
class VerifyCodeEvent(var code: Number) : SubmitEvent()
class ValidatePhoneNumberEvent(var number: String) : SubmitEvent()
class ValidateCodeEvent(var code: Number) : SubmitEvent()

class SubmitUiModel(var invalid : Boolean, var inProgress : Boolean,var success : Boolean,var codeSent :  Boolean = false ,var errorMessage : String? = null){
    var phoneNumber : Number = 0
    companion object {
        fun idle() = SubmitUiModel(false,false,false,false)
        fun invalid() = SubmitUiModel(true,false,false,false)
        fun inProgress() = SubmitUiModel(false,true,false)
        fun codeSent(to : Number) :SubmitUiModel {
            val submitUiModel = SubmitUiModel(false,false,false,true)
            submitUiModel.phoneNumber = to
            return submitUiModel
        }
        fun success() = SubmitUiModel(false,false,true)
        fun failure(errorMessage: String?) = SubmitUiModel(false,false,false,false, errorMessage?.split(".")?.get(0))
    }
}