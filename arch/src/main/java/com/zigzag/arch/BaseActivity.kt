package com.zigzag.arch

import android.arch.lifecycle.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by salah on 27/12/17.
 */

abstract class BaseActivity<I : BaseEvent, S : BaseViewState, VM : BaseViewModel<I, S>> : AppCompatActivity(), BaseView<I, S> {

    lateinit var viewModelFactory : ViewModelFactory

    lateinit var viewModel : VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelFactory = ViewModelFactory.getInstance(this)
    }

    override fun onStart() {
        super.onStart()
        bind()
    }

    private fun bind() {
        viewModel.states().subscribe(this::render)
        viewModel.processIntents(intents())
    }

    inline fun <reified T : VM> provideViewModel(vm : VM){
        viewModelFactory.registerViewModel(T::class.java, vm)
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(T::class.java)
    }

/*
    fun hideKeyboard(){
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    fun authRedirect(user : FirebaseUser?){
        val localClassNameArray = this.localClassName.split(".")
        val localClassName = localClassNameArray[localClassNameArray.size - 1]

        if(user == null){
            if(localClassName!=LoginActivity::class.java.simpleName) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        } else if(user.displayName.isNullOrEmpty()) {
            if(localClassName!=ProfileEditActivity::class.java.simpleName) {
                startActivity(Intent(this, ProfileEditActivity::class.java))
                finish()
            }
        } else {
            if(localClassName!=DashboardActivity::class.java.simpleName) {
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            }
        }
    }*/
}
