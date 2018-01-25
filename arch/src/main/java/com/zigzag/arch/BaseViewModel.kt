package com.zigzag.arch

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable

/**
 * Created by salah on 23/1/18.
 */

abstract class BaseViewModel<I : BaseEvent, S : BaseViewState> : ViewModel() {
    abstract fun processIntents(intents: Observable<I>)
    abstract fun states(): Observable<S>
}