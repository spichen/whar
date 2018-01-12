package com.zigzag.whar.rx.firebase

import com.google.firebase.FirebaseException

/**
 * Created by salah on 2/1/18.
 */

class RxFirebaseNullDataException : NullPointerException {
    constructor() {}
    constructor(detailMessage: String) : super(detailMessage)
}

class RxFirebaseAuthError : FirebaseException {
    constructor(detailMessage: String) : super(detailMessage)
    constructor()
}