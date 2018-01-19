package com.zigzag.whar.ui.profileEdit

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.zigzag.whar.arch.BasePresenter
import com.zigzag.whar.common.Constants.DATE_OF_BIRTH
import com.zigzag.whar.common.Constants.FIRST_NAME
import com.zigzag.whar.common.Constants.LAST_NAME
import com.zigzag.whar.common.Constants.PROFILE_IMAGE_FOLDER
import com.zigzag.whar.common.Constants.USER
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by salah on 13/1/18.
 */

class ProfileEditPresenter @Inject constructor() : BasePresenter<ProfileEditContract.View>(), ProfileEditContract.Presenter  {

    companion object {
        val TAG = "PEAPresenter"
    }

    override fun updateUserDetails(firstName: String, lastName : String, dob: String, image: Uri?) {

        val userData = HashMap<String,Any>()
        userData[FIRST_NAME] = firstName
        userData[LAST_NAME] = lastName
        userData[DATE_OF_BIRTH] = dob

        Observable.merge(
                rxFirebaseStorage
                        .uploadImage(PROFILE_IMAGE_FOLDER,FirebaseAuth.getInstance().currentUser?.uid!!, image!!)
                        .flatMap { uri -> rxFirebaseAuth.updateUserDetails(firstName + " " + lastName, uri) }
                        .toObservable(),
                rxFirebaseFirestore
                        .add(USER,FirebaseAuth.getInstance().currentUser?.uid!!,userData)
                        .toObservable()
        ).subscribe {
            view?.revertSubmitButtonAnimation()
            view?.onComplete()
        }.track()

    }

    override fun updateUserDetails(firstName: String, lastName : String, dob: String) {

        val userData = HashMap<String,Any>()
        userData[FIRST_NAME] = firstName
        userData[LAST_NAME] = lastName
        userData[DATE_OF_BIRTH] = dob

        Observable.merge(
                rxFirebaseAuth.updateUserDetails(firstName + " " + lastName).toObservable(),
                rxFirebaseFirestore.add(USER,FirebaseAuth.getInstance().currentUser?.uid!!,userData).toObservable()
        ).subscribe {
            view?.revertSubmitButtonAnimation()
            view?.onComplete()
        }.track()

    }
}