package com.zigzag.arch

import io.reactivex.Observable

/**
 * Created by salah on 24/1/18.
 */
interface BaseView<I : BaseEvent, in S : BaseViewState> {
    fun intents(): Observable<I>
    fun render(state: S)
}