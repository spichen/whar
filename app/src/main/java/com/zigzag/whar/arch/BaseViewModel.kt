package com.zigzag.whar.arch

import android.arch.lifecycle.ViewModel

/**
 * Created by salah on 27/12/17.
 */

class BaseViewModel<V : BaseContract.View, P : BaseContract.Presenter<V>> : ViewModel() {

    var presenter: P? = null

    override fun onCleared() {
        super.onCleared()
        presenter!!.onPresenterDestroy()
        presenter = null
    }
}