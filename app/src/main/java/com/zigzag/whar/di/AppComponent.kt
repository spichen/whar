package com.zigzag.whar.di

import android.app.Application

import com.zigzag.whar.App

import javax.inject.Singleton

import dagger.BindsInstance
import dagger.Component

import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

/**
 * Created by salah on 20/12/17.
 */

@Singleton
@Component(modules = arrayOf(ApplicationModule::class, ActivityBindingModule::class, AndroidSupportInjectionModule::class))
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): AppComponent.Builder
        fun build(): AppComponent
    }
}