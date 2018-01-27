package com.zigzag.riverx

import io.reactivex.Observable

/**
 * Created by salah on 24/1/18.
 */

interface RiveRxView<E : RiveRxEvent, in VS : RiveRxViewState> {

    val riveRx : RiveRxDelegate
    fun localEvents(): Observable<E>
    fun events(): Observable<E>
    fun render(state: VS)

}