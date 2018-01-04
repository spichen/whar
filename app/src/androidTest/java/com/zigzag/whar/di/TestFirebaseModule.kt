package com.zigzag.whar.di

import com.google.firebase.auth.PhoneAuthProvider
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.zigzag.whar.rx.firebase.RxFirebaseAuth

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import io.reactivex.Observable
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

}