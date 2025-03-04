package com.firebaseserviceandroidapp.features.login.ui.screen

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import com.firebaseserviceandroidapp.R
import com.firebaseserviceandroidapp.core.base.activity.BaseActivity
import com.firebaseserviceandroidapp.core.layout.ui.screen.MainLayoutActivity
import com.firebaseserviceandroidapp.databinding.ActivityLoginBinding
import com.firebaseserviceandroidapp.features.login.logic.LoginViewModel
import com.firebaseserviceandroidapp.features.login.logic.LoginNavigator
import com.firebaseserviceandroidapp.features.register.ui.screen.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(), LoginNavigator {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vmLogin = loginViewModel
        loginViewModel.viewState = this
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_login
    }

    override fun makeViewModelProvider(): LoginViewModel {
        return loginViewModel
    }

    override fun openRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        val options = ActivityOptionsCompat.makeCustomAnimation(
            this,
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
        startActivity(intent, options.toBundle())
    }

    override fun openHome() {
        val intent = Intent(this, MainLayoutActivity::class.java)
        val options = ActivityOptionsCompat.makeCustomAnimation(
            this,
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
        startActivity(intent, options.toBundle())
        finish()
    }
}