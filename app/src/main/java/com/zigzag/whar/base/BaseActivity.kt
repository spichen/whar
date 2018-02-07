package com.zigzag.whar.base

import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseUser
import com.zigzag.whar.rx.firebase.RxFirebaseAuth
import com.zigzag.whar.ui.dashboard.DashboardActivity
import com.zigzag.whar.ui.login.LoginActivity
import com.zigzag.whar.ui.profileEdit.ProfileEditActivity
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

open class BaseActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var rxFirebaseAuth : RxFirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rxFirebaseAuth.observeAuthState()
                .subscribe { //firebaseAuth -> authRedirect(firebaseAuth.currentUser)
                     }
    }

    private fun authRedirect(user : FirebaseUser?){

        val localClassNameArray = this.localClassName.split(".")
        val localClassName = localClassNameArray[localClassNameArray.size - 1]

        when {
            user == null ->  {
                if(localClassName!=LoginActivity::class.java.simpleName){
                    startActivity(Intent(this,LoginActivity::class.java))
                    finish()
                }
            }
            user.displayName.isNullOrEmpty() -> {
                if(localClassName!=ProfileEditActivity::class.java.simpleName){
                    startActivity(Intent(this,ProfileEditActivity::class.java))
                    finish()
                }
            }
            else -> {
                if(localClassName!=DashboardActivity::class.java.simpleName){
                    startActivity(Intent(this,DashboardActivity::class.java))
                    finish()
                }
            }
        }
    }
}
