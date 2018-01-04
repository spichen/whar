package com.zigzag.whar.di

import com.zigzag.whar.TestApplication

import javax.inject.Singleton

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

/**
 * Created by salah on 3/1/18.
 */

@Singleton
@Component(modules = arrayOf(
        ApplicationModule::class,
        TestFirebaseModule::class,
        AndroidSupportInjectionModule::class,
        ActivityBindingModule::class
))
interface TestComponent : AndroidInjector<TestApplication> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<TestApplication>()
}
