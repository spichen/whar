package com.zigzag.whar.di

/**
 * Created by salah on 3/1/18.
 */

import com.google.firebase.auth.PhoneAuthProvider
import com.zigzag.whar.rx.firebase.RxFirebaseAuth

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import io.reactivex.Observable

/**
 * Created by salah on 21/12/17.
 */

@Module
class FirebaseModule {

    @Provides
    @Singleton
    internal fun provideRxFirebaseAuth(): RxFirebaseAuth {
        return RxFirebaseAuth()
    }

}