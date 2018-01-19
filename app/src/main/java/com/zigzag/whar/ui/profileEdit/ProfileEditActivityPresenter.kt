package com.zigzag.whar.ui.profileEdit

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.zigzag.whar.arch.BasePresenter
import com.zigzag.whar.common.Constants.PROFILE_IMAGE_FOLDER
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by salah on 13/1/18.
 */

class ProfileEditActivityPresenter @Inject constructor() : BasePresenter<ProfileEditActivityContract.View>(), ProfileEditActivityContract.Presenter  {

    companion object {
        val TAG = "PEAPresenter"
    }

    override fun updateUserDetails(firstName: String, lastName : String, dob: String, image: Uri?) {

        val userData = HashMap<String,Any>()
        userData["firstName"] = firstName
        userData["lastName"] = lastName
        userData["dateOfBirth"] = dob

        Observable.merge(
                rxFirebaseStorage
                        .uploadImage(PROFILE_IMAGE_FOLDER,FirebaseAuth.getInstance().currentUser?.uid!!, image!!)
                        .flatMap { uri -> rxFirebaseAuth.updateUserDetails(firstName + " " + lastName, uri) }
                        .toObservable(),
                rxFirebaseFirestore
                        .add("user",FirebaseAuth.getInstance().currentUser?.uid!!,userData)
                        .toObservable()
        ).subscribe {
            view?.revertSubmitButtonAnimation()
            view?.onComplete()
        }.track()

    }

    override fun updateUserDetails(firstName: String, lastName : String, dob: String) {

        val userData = HashMap<String,Any>()
        userData["firstName"] = firstName
        userData["lastName"] = lastName
        userData["dateOfBirth"] = dob


        Observable.merge(
                rxFirebaseAuth.updateUserDetails(firstName + " " + lastName).toObservable(),
                rxFirebaseFirestore.add("user",FirebaseAuth.getInstance().currentUser?.uid!!,userData).toObservable()
        ).subscribe {
            view?.revertSubmitButtonAnimation()
            view?.onComplete()
        }.track()

    }
}