package com.zigzag.whar.rx.firebase
import android.util.Log
import com.google.android.gms.tasks.TaskExecutors.MAIN_THREAD
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import io.reactivex.Maybe
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by salah on 2/1/18.
 */
/*

@Suppress("NOTHING_TO_INLINE")
public inline fun PhoneAuthProvider.rxVerifyPhoneNumber(number: Number) : Observable<Any>
        = RxFirebaseAuth.phoneAuthProvider(this,number)
*/

data class VerificationData(val verificationId: String, val token: PhoneAuthProvider.ForceResendingToken)

open class RxFirebaseAuth {

    val TAG = "RxFirebaseAuth"

    fun signInWithEmailAndPassword(firebaseAuth: FirebaseAuth,
                                   email: String,
                                   password: String): Maybe<AuthResult> {
        return Maybe.create { emitter -> RxHandler.assignOnTask(emitter, firebaseAuth.signInWithEmailAndPassword(email, password)) }
    }

    open fun phoneAuthProvider(number: Number): Observable<Any> {
        return Observable.create { emitter ->
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    number.toString(),
                    60,
                    TimeUnit.SECONDS,
                    MAIN_THREAD,
                    object :PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                            emitter.onNext(credential)
                            emitter.onComplete()
                        }

                        override fun onVerificationFailed(e: FirebaseException) {
                            emitter.onError(RxFirebaseAuthError(e.localizedMessage))
                        }

                        override fun onCodeSent(verificationId: String?,
                                                token: PhoneAuthProvider.ForceResendingToken) {
                            emitter.onNext(VerificationData(verificationId!!,token))
                        }
                })
        }
    }
}
