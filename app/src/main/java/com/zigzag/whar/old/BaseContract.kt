package com.zigzag.whar.old

import android.arch.lifecycle.Lifecycle
import android.os.Bundle
import com.google.firebase.auth.FirebaseUser

/**
 * Created by salah on 27/12/17.
 */

interface BaseContract {

    interface View{
        fun authRedirect(user : FirebaseUser?)
    }

    interface Presenter<V : View> {

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