package com.zigzag.whar.di

import android.support.test.espresso.core.internal.deps.guava.collect.Iterables.toArray
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.zigzag.whar.rx.firebase.RxFirebaseAuth
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
        return mock<RxFirebaseAuth> {
            on { phoneAuthProvider(919895940989) } doReturn Observable.just(credential as Any)
            on { phoneAuthProvider(919747797987) } doReturn Observable.just(VerificationData("asasas",forceResendToken) as Any)
            on { phoneAuthProvider(910000000) } doReturn Observable.error(Exception("The format of the phone number provided is incorrect"))
        }
    }

}