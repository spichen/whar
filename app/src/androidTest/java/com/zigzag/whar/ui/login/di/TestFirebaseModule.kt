package com.zigzag.whar.ui.login.di

import com.google.firebase.auth.PhoneAuthProvider

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

/**
 * Created by salah on 3/1/18.
 */

@Module
internal class TestFirebaseModule {
    var `fun`: internal? = null

    init {
        return PhoneAuthProvider.getInstance()
    }
}