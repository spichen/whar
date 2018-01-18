package com.zigzag.whar.rx.firebase

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Maybe

/**
 * Created by salah on 14/1/18.
 */

open class RxFirebaseStorage {

    var storageRef = FirebaseStorage.getInstance().reference

    open fun uploadImage(folder : String, filename : String, image: Uri): Maybe<Uri> {
        return Maybe.create { emitter ->
            val uploadTask = storageRef.child(folder).child(filename).putFile(image)
            uploadTask.addOnFailureListener( { exception ->
                emitter.onError(RxFirebaseStorageError(exception.localizedMessage))
            }).addOnSuccessListener({ taskSnapshot ->
                emitter.onSuccess(taskSnapshot.downloadUrl!!)
            })
        }
    }
}