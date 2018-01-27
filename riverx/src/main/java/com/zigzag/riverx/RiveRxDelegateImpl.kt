package com.zigzag.riverx

/**
 * Created by salah on 27/1/18.
 */
class RiveRxDelegateImpl<E : RiveRxEvent,A : RiveRxAction, out P : RiveRxProcessor<A, R>, R : RiveRxResult, VS : RiveRxViewState, out VM : RiveRxViewModel<E, A, P, R, VS>>
(val riverx : RiveRxView<E,A,P,R,VS,VM>):
        RiveRxDelegate {
    override fun onStart() {
        riverx.viewModel.states().subscribe ({viewState -> riverx.render(viewState)}){ t -> throw t}
        riverx.viewModel.processEvents(riverx.intents())
    }
}