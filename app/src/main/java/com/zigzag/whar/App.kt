package com.zigzag.whar

import com.rxfuel.rxfuel.RxFuel
import com.zigzag.whar.data.firebaseProcessor.FirebaseProcessorModule
import com.zigzag.whar.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject

open class App : DaggerApplication() {

    @Inject
    lateinit var firebaseProcessorModule : FirebaseProcessorModule

    override fun onCreate() {
        super.onCreate()
        RxFuel.registerProcessorModule(firebaseProcessorModule)
    }

    override fun applicationInjector(): AndroidInjector<App> {
        return DaggerAppComponent
                    .builder()
                    .create(this@App)
    }

}
