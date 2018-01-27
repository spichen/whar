package com.zigzag.riverx

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

/**
 * Created by salah on 23/1/18.
 */

abstract class RiveRxViewModel<E : RiveRxEvent, out A : RiveRxAction, R : RiveRxResult,  VS : RiveRxViewState>(private val processor : RiveRxProcessor<A,R>) : ViewModel() {

    abstract var idleState : VS

    private val eventsSubject: PublishSubject<E> = PublishSubject.create()
    private val localEventsSubject: PublishSubject<E> = PublishSubject.create()
    private val statesObservable: Observable<VS> = compose()

    fun processEvents(events: Observable<E>, localEvents : Observable<E>) {
        events.subscribe(eventsSubject)
        localEvents.subscribe(localEventsSubject)
    }

    fun states(): Observable<VS> = statesObservable

    private fun compose(): Observable<VS> {
        return Observable.merge(
                eventsSubject
                    .compose<E>(intentFilter())
                    .map(this::eventToAction)
                    .compose(processor.process()),
                localEventsSubject
                    .map(this::eventToResult)
                )
                .scan(idleState, resultToViewState())
                .replay(1)
                .autoConnect(0)
    }

    abstract fun eventToResult(event: E) : R

    abstract fun intentFilter(): ObservableTransformer<E, E>

    abstract fun eventToAction(event: E): A

    abstract fun resultToViewState() : BiFunction<VS,R,VS>
}