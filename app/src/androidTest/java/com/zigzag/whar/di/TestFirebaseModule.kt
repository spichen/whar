package com.zigzag.whar.di

import com.google.firebase.auth.PhoneAuthProvider
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.zigzag.whar.rx.firebase.RxFirebaseAuth

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

/**
 * Created by salah on 3/1/18.
 */

@Module
class TestFirebaseModule {

    @Provides
    @Singleton
    internal fun provideRxFirebaseAuth(): RxFirebaseAuth {
        val credential = PhoneAuthProvider.getCredential("234234", "234234")
        return mock<RxFirebaseAuth> {
            on { phoneAuthProvider(919895940989) } doReturn Observable.just(credential as Any)
        }
    }

/*
    @Provides
    @Singleton
    internal fun providePhoneAuthProvider(rxFirebaseAuth : RxFirebaseAuth): PhoneAuthProvider {
        val phoneAuthProvider = Mockito.mock(PhoneAuthProvider::class.java)
        val credential = PhoneAuthProvider.getCredential("234234", "234234")
        `when`(rxFirebaseAuth.phoneAuthProvider(phoneAuthProvider,919895940989)).thenReturn(Observable.just(credential))

        val moack = mock<PhoneAuthProvider> {
            on { r.phoneAuthProvider()rxVerifyPhoneNumber(919895940989) } doReturn Observable.just(credential as Any)
        }
        val mock = mock<PhoneAuthProvider> {
            when { phoneAuthProvider.rxVerifyPhoneNumber(919895940989) } thenReturn "text"
        }

        when(phoneAuthProvider.rxVerifyPhoneNumber(919895940989)).Mockito.thenReturn(
                just(UserResponse.create(
                        User.create(1, 200, "user 1"),
                        User.create(2, 100, "user 2")
                ))
        );

        return phoneAuthProvider
    }
*/

}