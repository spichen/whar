package com.zigzag.whar.di

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.zigzag.whar.rx.firebase.*

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import org.mockito.Mockito

/**
 * Created by salah on 3/1/18.
 */

@Module
class TestFirebaseModule {

    lateinit var authStateMockInterface : AuthStateMockInterface

    @Provides
    @Singleton
    internal fun provideRxFirebaseAuth(): RxFirebaseAuth {
        val credential = Mockito.mock(PhoneAuthCredential::class.java)
        val forceResendToken = Mockito.mock(PhoneAuthProvider.ForceResendingToken::class.java)
        val firebaseUser = mock<FirebaseUser> {
            on { uid } doReturn "123abc"
        }

        val loggedInFirebaseAuth = mock<FirebaseAuth>{
            on { currentUser } doReturn firebaseUser
        }
        val loggedOutFirebaseAuth = Mockito.mock(FirebaseAuth::class.java)

        return mock<RxFirebaseAuth> {
            on { phoneAuthProvider(919895940989) } doAnswer {
                authStateMockInterface.login()
                Observable.just(credential as Any)
            }
            on { phoneAuthProvider(919747797987) } doReturn Observable.just(VerificationData("himalaya",forceResendToken) as Any)
            on { signInWithCode("himalaya","123456")  } doAnswer {
                authStateMockInterface.login()
                return@doAnswer Completable.complete()
            }
            on { signInWithCode("himalaya","123451") } doReturn Completable.error(RxFirebaseAuthError())
            on { phoneAuthProvider(910000000000) } doReturn Observable.error(RxFirebaseAuthError("The format of the phone number provided is incorrect"))
            on { observeAuthState() } doReturn Observable.create<FirebaseAuth> { emitter ->
                authStateMockInterface = object : AuthStateMockInterface {
                    override fun login() {
                        emitter.onNext(loggedInFirebaseAuth)
                    }
                    override fun logout() {
                        emitter.onNext(loggedOutFirebaseAuth)
                    }
                }
            }
            on { updateUserDetails("Sal Sam") } doReturn Maybe.create<String> { emitter ->
                emitter.onSuccess("Updated")
            }
        }
    }

    @Provides
    @Singleton
    internal fun provideRxFirebaseStorage(): RxFirebaseStorage {
        val uri = Mockito.mock(Uri::class.java)
        return mock<RxFirebaseStorage> {
            on { uploadImage("profile_images","123abc", uri) } doAnswer {
                Maybe.just(uri)
            }
        }
    }

    @Provides
    @Singleton
    internal fun provideRxFirebaseFirestore(): RxFirebaseFirestore {

        val userData = HashMap<String,Any>()
        userData["firstName"] = "Sal"
        userData["lastName"] = "Sam"
        userData["dateOfBirth"] = "28 May 1993"

        return mock<RxFirebaseFirestore> {
            on { add(any<String>(), any<String>(), any<HashMap<String,Any>>()) } doAnswer {
                Completable.complete()
            }
        }
    }

    interface AuthStateMockInterface{
        fun login()
        fun logout()
    }
}