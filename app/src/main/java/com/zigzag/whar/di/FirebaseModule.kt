package com.zigzag.whar.di

import com.zigzag.whar.rx.firebase.RxFirebaseAuth
import com.zigzag.whar.rx.firebase.RxFirebaseFirestore
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

    @Provides
    @Singleton
    internal fun provideRxFirebaseFirestore(): RxFirebaseFirestore {
        return RxFirebaseFirestore()
    }

}