package com.zigzag.whar.rx.firebase

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Maybe
import java.io.ByteArrayOutputStream


/**
 * Created by salah on 14/1/18.
 */
open class RxFirebaseStorage {

    var storageRef = FirebaseStorage.getInstance().reference

    open fun uploadImage(folder : String, filename : String, image: Bitmap): Maybe<Uri> {
        return Maybe.create { emitter ->
            val baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            val uploadTask = storageRef.child(folder).child(filename).putBytes(data)
            uploadTask.addOnFailureListener( { exception ->
                emitter.onError(RxFirebaseStorageError(exception.localizedMessage))
            }).addOnSuccessListener({ taskSnapshot ->
                emitter.onSuccess(taskSnapshot.downloadUrl!!)
            })
        }
    }
}