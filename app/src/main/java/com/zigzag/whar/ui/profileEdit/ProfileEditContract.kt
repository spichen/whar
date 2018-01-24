package com.zigzag.whar.ui.profileEdit

import android.net.Uri
import com.zigzag.whar.old.BaseContract

/**
 * Created by salah on 13/1/18.
 */

class ProfileEditContract {
    interface View : BaseContract.View {
        fun disableSubmitButton()
        fun enableSubmitButton()
        fun startSubmitButtonAnimation()
        fun revertSubmitButtonAnimation()
        fun onComplete()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun updateUserDetails(firstName : String, lastName : String, dob : String, image : Uri?)
        fun updateUserDetails(firstName : String, lastName : String, dob : String)
    }
}