package com.zigzag.whar.rx.firebase
import com.google.android.gms.tasks.TaskExecutors.MAIN_THREAD
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import io.reactivex.Maybe
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.OnCompleteListener

/**
 * Created by salah on 2/1/18.
 */

/*
@Suppress("NOTHING_TO_INLINE")
public inline fun PhoneAuthProvider.rxVerifyPhoneNumber(number: Number) : Observable<Any> = RxFirebaseAuth.phoneAuthProvider(this,number)
*/

data class VerificationData(val verificationId: String, val token: PhoneAuthProvider.ForceResendingToken)

open class RxFirebaseAuth {

    val TAG = "RxFirebaseAuth"

    lateinit var phoneAuthCallback : PhoneAuthProvider.OnVerificationStateChangedCallbacks

    fun signInWithEmailAndPassword(firebaseAuth: FirebaseAuth,
                                   email: String,
                                   password: String): Maybe<AuthResult> {
        return Maybe.create { emitter -> RxHandler.assignOnTask(emitter, firebaseAuth.signInWithEmailAndPassword(email, password)) }
    }

    open fun phoneAuthProvider(number: Number): Observable<Any> {
        return Observable.create { emitter ->
            phoneAuthCallback = object :PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
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
            }
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    number.toString(),
                    60,
                    TimeUnit.SECONDS,
                    MAIN_THREAD,
                    phoneAuthCallback)
        }
    }

    open fun verifyPhoneNumber(verificationId : String, code: String): Observable<FirebaseUser> {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        return Observable.create { emitter ->
            FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener(MAIN_THREAD, OnCompleteListener<AuthResult> { task ->
                        if (task.isSuccessful) {
                            emitter.onNext(task.result.user)
                            emitter.onComplete()
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.exception)
                            if (task.exception is FirebaseAuthInvalidCredentialsException) {
                                emitter.onError(RxFirebaseAuthError())
                            }
                        }
                    })
        }
    }

    open fun resendCode(number: Number, token: PhoneAuthProvider.ForceResendingToken) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number.toString(),
                60,
                TimeUnit.SECONDS,
                MAIN_THREAD,
                phoneAuthCallback,
                token)
    }
}
