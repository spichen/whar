package com.zigzag.whar.di

import com.zigzag.whar.App

import javax.inject.Singleton
import dagger.Component

import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

/**
 * Created by salah on 20/12/17.
 */

@Singleton
@Component(modules = arrayOf(
        ApplicationModule::class,
        FirebaseModule::class,
        ViewModelModule::class,
        AndroidSupportInjectionModule::class,
        ActivityBindingModule::class
))
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}