package com.zigzag.whar.arch

import android.arch.lifecycle.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.inputmethod.InputMethodManager

import dagger.android.support.DaggerAppCompatActivity
import com.google.firebase.auth.FirebaseUser
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle
import com.trello.rxlifecycle2.LifecycleProvider
import com.zigzag.whar.ui.dashboard.DashboardActivity
import com.zigzag.whar.ui.login.LoginActivity
import com.zigzag.whar.ui.profileEdit.ProfileEditActivity
import javax.inject.Inject

/**
 * Created by salah on 27/12/17.
 */

abstract class BaseActivity<I : BaseIntent, S : BaseViewState, VM : BaseViewModel<I,S>> : DaggerAppCompatActivity(), BaseView<I, S>{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel : VM

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        bind()
    }

    private fun bind() {
        viewModel.states().subscribe(this::render)
        viewModel.processIntents(intents())
    }

    val provider: LifecycleProvider<Lifecycle.Event>
        get() = AndroidLifecycle.createLifecycleProvider(this)

    inline fun <reified T : VM> provideViewModel() = ViewModelProviders.of(this, viewModelFactory).get(T::class.java)

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
    }

}
