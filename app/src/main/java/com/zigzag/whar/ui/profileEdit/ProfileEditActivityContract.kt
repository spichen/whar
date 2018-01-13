package com.zigzag.whar.ui.profileEdit

import android.graphics.Bitmap
import com.zigzag.whar.arch.BaseContract

/**
 * Created by salah on 13/1/18.
 */
class ProfileEditActivityContract {
    interface View : BaseContract.View {
        fun startSubmitButtonAnimation()
        fun revertSubmitButtonAnimation()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun updateUserDetails(name : String, image : Bitmap)
    }

}