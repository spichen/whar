package com.salah.riverx

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity

/**
 * Created by salah on 27/1/18.
 */

class RiveRxDelegateImpl<E : RiveRxEvent,
        out A : RiveRxAction,
        R : RiveRxResult,
        VS : RiveRxViewState,
        out VM : RiveRxViewModel<E, A, R, VS>> (val context : FragmentActivity, private val viewModelNewInstance: VM) : RiveRxDelegate {

    init {
        ViewModelFactory.getInstance().registerViewModel(viewModelNewInstance)
    }

    private val viewModel : VM by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders
                .of(context, ViewModelFactory.getInstance())
                .get(viewModelNewInstance.javaClass)
    }

    override fun onStart() {
        viewModel.states().subscribe ({viewState -> (context as RiveRxView<E,VS>).render(viewState)}){ t -> throw t}
        viewModel.processEvents((context as RiveRxView<E,VS>).events(),(context as RiveRxView<E,VS>).localEvents())
    }

}