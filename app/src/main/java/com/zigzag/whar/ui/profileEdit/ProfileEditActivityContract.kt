package com.zigzag.whar.ui.profileEdit

import android.graphics.Bitmap
import com.zigzag.whar.arch.BaseContract

/**
 * Created by salah on 13/1/18.
 */
class ProfileEditActivityContract {
    interface View : BaseContract.View {
        fun disableSubmitButton()
        fun enableSubmitButton()
        fun startSubmitButtonAnimation()
        fun revertSubmitButtonAnimation()
        fun onComplete()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun updateUserDetails(name : String, dob : String, image : Bitmap)
    }

}