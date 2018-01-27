package com.zigzag.riverx

/**
 * Created by salah on 27/1/18.
 */

class RiveRxDelegateImpl<E : RiveRxEvent,
        A : RiveRxAction,
        out P : RiveRxProcessor<A, R>,
        R : RiveRxResult, VS : RiveRxViewState,
        out VM : RiveRxViewModel<E, A, R, VS>> (private val riveRxView : RiveRxView<E,VS>, private val viewModel : VM) : RiveRxDelegate {

    override fun onStart() {
        viewModel.states().subscribe ({viewState -> riveRxView.render(viewState)}){ t -> throw t}
        viewModel.processEvents(riveRxView.intents())
    }

}