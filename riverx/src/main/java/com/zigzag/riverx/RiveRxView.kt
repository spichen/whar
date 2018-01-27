package com.zigzag.riverx

import io.reactivex.Observable

/**
 * Created by salah on 24/1/18.
 */
interface RiveRxView<E : RiveRxEvent, A : RiveRxAction, out P :RiveRxProcessor<A,R>,R : RiveRxResult, VS : RiveRxViewState, out VM : RiveRxViewModel<E, A, P, R, VS>> {
    val riverx : RiveRxDelegate
    val viewModel : VM
    fun intents(): Observable<E>
    fun render(state: VS)
}