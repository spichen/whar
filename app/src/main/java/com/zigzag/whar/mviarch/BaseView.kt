package com.zigzag.whar.mviarch

import io.reactivex.Observable

/**
 * Created by salah on 24/1/18.
 */
interface BaseView<I : BaseIntent, in S : BaseViewState> {
    fun intents(): Observable<I>
    fun render(state: S)
}