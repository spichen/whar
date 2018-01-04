package com.zigzag.whar

import android.support.test.runner.AndroidJUnitRunner
import android.app.Application
import android.app.Instrumentation
import android.content.Context


/**
 * Created by salah on 4/1/18.
 */
class MockJUnitRunner : AndroidJUnitRunner() {
    @Throws(InstantiationException::class, IllegalAccessException::class, ClassNotFoundException::class)
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return Instrumentation.newApplication(TestApplication::class.java, context)
    }
}