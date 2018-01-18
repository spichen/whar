package com.zigzag.whar.rx.firebase

import io.reactivex.Maybe
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentReference
import io.reactivex.Completable

/**
 * Created by salah on 14/1/18.
 */

open class RxFirebaseFirestore {

    companion object {
        val TAG = "RxFirebaseFirestore"
    }

    var db = FirebaseFirestore.getInstance()

    open fun add(collection : String, data : HashMap<String, Any>): Maybe<DocumentReference> {
        return Maybe.create { emitter ->
            db.collection(collection)
                    .add(data)
                    .addOnSuccessListener { documentReference ->
                        emitter.onSuccess(documentReference)
                    }
                    .addOnFailureListener { e ->
                        emitter.onError(e)
                    }
        }
    }

    open fun add(collection : String, document : String, data : HashMap<String, Any>): Completable {
        return Completable.create { emitter ->
            db.collection(collection)
                    .document(document)
                    .set(data)
                    .addOnSuccessListener {
                        emitter.onComplete()
                    }
                    .addOnFailureListener { e ->
                        emitter.onError(e)
                    }
        }
    }
}