package com.zigzag.whar.ui.login

import com.zigzag.whar.arch.BaseContract

/**
 * Created by salah on 30/12/17.
 */

class LoginActivityContract {
    interface View : BaseContract.View {
        fun showLoginError(error: String)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun attemptLogin(username: String, password: String)
        fun validate(username: String, password: String) : Boolean

    }
}
