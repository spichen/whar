package com.zigzag.whar.di

/**
 * Created by salah on 3/1/18.
 */

import com.zigzag.whar.rx.firebase.RxFirebaseAuth
import com.zigzag.whar.rx.firebase.RxFirebaseStorage

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

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

    @Provides
    @Singleton
    internal fun provideRxFirebaseStorage(): RxFirebaseStorage {
        return RxFirebaseStorage()
    }

}