package com.zigzag.whar.ui.profileEdit

import android.graphics.Bitmap
import com.zigzag.whar.arch.BasePresenter
import javax.inject.Inject

/**
 * Created by salah on 13/1/18.
 */
class ProfileEditActivityPresenter @Inject constructor() : BasePresenter<ProfileEditActivityContract.View>(), ProfileEditActivityContract.Presenter  {

    val TAG = "ProfileEditActivityPresenter"

    override fun updateUserDetails(name: String, image: Bitmap) {
        rxFirebaseAuth
                .updateUserDetails(name,image)
                .subscribe{
                    view?.revertSubmitButtonAnimation()
                }
    }

}