package com.zigzag.riverx

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

/**
 * Created by salah on 23/1/18.
 */

abstract class RiveRxViewModel<E : RiveRxEvent, A : RiveRxAction, out P : RiveRxProcessor<A,R>, R : RiveRxResult,  VS : RiveRxViewState>(val processor : P) : ViewModel() {

    abstract var idleState : VS

    private val intentsSubject: PublishSubject<E> = PublishSubject.create()
    private val statesObservable: Observable<VS> = compose()

    fun processEvents(events: Observable<E>) {
        events.subscribe(intentsSubject)
    }

    fun states(): Observable<VS> = statesObservable

    private fun compose(): Observable<VS> {
        return intentsSubject
                .compose<E>(intentFilter())
                .map(this::eventToAction)
                .compose(processor.process())
                .scan(idleState, resultToViewState())
                .replay(1)
                .autoConnect(0)
    }

    abstract fun intentFilter(): ObservableTransformer<E, E>

    abstract fun eventToAction(event: E): A

    abstract fun resultToViewState() : BiFunction<VS,R,VS>
}