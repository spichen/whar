package com.zigzag.whar

import com.zigzag.whar.di.DaggerTestComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

/**
 * Created by salah on 4/1/18.
 */
class TestApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<TestApplication> {
        return DaggerTestComponent
                .builder()
                .create(this@TestApplication)
    }
}