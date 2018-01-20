package com.zigzag.whar.rx.firebase

import android.net.Uri
import android.support.annotation.NonNull
import com.google.android.gms.tasks.TaskExecutors.MAIN_THREAD
import com.google.firebase.FirebaseException
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Maybe
import com.google.firebase.auth.UserProfileChangeRequest
import io.reactivex.Completable

/**
 * Created by salah on 2/1/18.
 */

open class RxFirebaseAuth {

    val TAG = "RxFirebaseAuth"

    lateinit var phoneAuthCallback : PhoneAuthProvider.OnVerificationStateChangedCallbacks

    @NonNull
    open fun observeAuthState(): Observable<FirebaseAuth> {
        return Observable.create { emitter ->
            val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth -> emitter.onNext(firebaseAuth) }
            FirebaseAuth.getInstance().addAuthStateListener(authStateListener)
            emitter.setCancellable { FirebaseAuth.getInstance().removeAuthStateListener(authStateListener) }
        }
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

    open fun resendCode(number: Number, token: PhoneAuthProvider.ForceResendingToken) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            number.toString(),
            60,
            TimeUnit.SECONDS,
            MAIN_THREAD,
            phoneAuthCallback,
            token)
    }

    open fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential): Completable {
        return Completable.create { emitter ->
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(MAIN_THREAD, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
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

    open fun signInWithCode(verificationId: String, code : String): Completable {
        return signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(verificationId, code))
    }

    open fun updateUserDetails(name : String, image : Uri? = null): Maybe<String> {
        return Maybe.create{ emitter ->

            val user = FirebaseAuth.getInstance().currentUser

            val profileUpdates =
                    if(image!=null)
                        UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .setPhotoUri(image)
                        .build()
                    else
                        UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()

            user!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        emitter.onSuccess("updated")
                        Log.d(TAG, "User profile updated.")
                    }else {
                        emitter.onError(RxFirebaseAuthError(task.exception?.localizedMessage!!))
                    }
                }
        }
    }
}

data class VerificationData(val verificationId: String, val token: PhoneAuthProvider.ForceResendingToken)

