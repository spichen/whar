package com.zigzag.whar.arch

import android.arch.lifecycle.Lifecycle
import android.os.Bundle

/**
 * Created by salah on 27/12/17.
 */

interface BaseContract {

    interface View

    interface Presenter<V : BaseContract.View> {

        val stateBundle: Bundle?

        val view: V?

        val isViewAttached: Boolean

        fun attachLifecycle(lifecycle: Lifecycle)

        fun detachLifecycle(lifecycle: Lifecycle)

        fun attachView(view: V)

        fun detachView()

        fun onPresenterCreated()

        fun onPresenterDestroy()
    }
}