package com.zigzag.whar.di

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.zigzag.whar.rx.firebase.RxFirebaseAuth
import com.zigzag.whar.rx.firebase.RxFirebaseAuthError
import com.zigzag.whar.rx.firebase.VerificationData

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import org.mockito.Mockito

/**
 * Created by salah on 3/1/18.
 */

@Module
class TestFirebaseModule {
    @Provides
    @Singleton
    internal fun provideRxFirebaseAuth(): RxFirebaseAuth {
        val credential = Mockito.mock(PhoneAuthCredential::class.java)
        val forceResendToken = Mockito.mock(PhoneAuthProvider.ForceResendingToken::class.java)
        val firebaseUser = Mockito.mock(FirebaseUser::class.java)
        return mock<RxFirebaseAuth> {
            on { phoneAuthProvider(919895940989) } doReturn Observable.just(credential as Any)
            on { phoneAuthProvider(919747797987) } doReturn Observable.just(VerificationData("himalaya",forceResendToken) as Any)
            on { verifyPhoneNumber("himalaya","123456") } doReturn Observable.just(firebaseUser)
            on { verifyPhoneNumber("himalaya","123451") } doReturn Observable.error(RxFirebaseAuthError())
            on { phoneAuthProvider(910000000000) } doReturn Observable.error(RxFirebaseAuthError("The format of the phone number provided is incorrect"))
        }
    }
}