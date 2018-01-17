package com.zigzag.whar.ui.profileEdit

import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.zigzag.whar.arch.BasePresenter
import com.zigzag.whar.common.Constants.PROFILE_IMAGE_FOLDER
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by salah on 13/1/18.
 */
class ProfileEditActivityPresenter @Inject constructor() : BasePresenter<ProfileEditActivityContract.View>(), ProfileEditActivityContract.Presenter  {

    val TAG = "ProfileEditActivityPresenter"

    // @TODO add dob in firestore
    override fun updateUserDetails(name: String, dob: String, image: Bitmap) {
        rxFirebaseStorage
                .uploadImage(PROFILE_IMAGE_FOLDER,FirebaseAuth.getInstance().currentUser?.uid!!,image)
                .observeOn(Schedulers.io())
                .flatMap { uri -> rxFirebaseAuth.updateUserDetails(name, uri) }
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    view?.revertSubmitButtonAnimation()
                    view?.onComplete()
                }
    }

}