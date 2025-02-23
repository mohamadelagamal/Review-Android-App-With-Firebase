package com.firebaseserviceandroidapp.features.splash.ui.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import com.firebaseserviceandroidapp.R
import com.firebaseserviceandroidapp.core.layout.ui.screen.MainLayoutActivity
import com.firebaseserviceandroidapp.core.utils.NetworkUtil
import com.firebaseserviceandroidapp.core.utils.NetworkUtil.getUser
import com.firebaseserviceandroidapp.features.login.ui.screen.LoginActivity
import com.firebaseserviceandroidapp.features.register.data.models.RegisterResponse
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorAccent)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
           checkLoginInFirebase()
        },2000)

    }
    private fun checkLoginInFirebase() {
        val firebaseUser = Firebase.auth.currentUser
        when{
            firebaseUser==null->{
                openLoginAccount()
            }
            else->{
                getUser(firebaseUser.uid, OnSuccessListener { it ->
                    // sort of user data
                    openHome()
                }, OnFailureListener {
                    openLoginAccount()
                })
            }
        }
    }

     fun openHome() {
         val intent = Intent(this, MainLayoutActivity::class.java)
         val options = ActivityOptionsCompat.makeCustomAnimation(
             this,
             android.R.anim.fade_in,
             android.R.anim.fade_out
         )
         startActivity(intent, options.toBundle())
        finish()
    }

    private fun openLoginAccount() {
        val intent = Intent(this, LoginActivity::class.java)
        val options = ActivityOptionsCompat.makeCustomAnimation(
            this,
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
        startActivity(intent, options.toBundle())
        finish()
    }
}
