package com.firebaseserviceandroidapp.features.register.ui.screen

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import com.firebaseserviceandroidapp.R
import com.firebaseserviceandroidapp.core.base.activity.BaseActivity
import com.firebaseserviceandroidapp.core.layout.ui.screen.MainLayoutActivity
import com.firebaseserviceandroidapp.databinding.ActivityRegisterBinding
import com.firebaseserviceandroidapp.features.login.ui.screen.LoginActivity
import com.firebaseserviceandroidapp.features.register.logic.RegisterViewModel
import com.firebaseserviceandroidapp.features.register.logic.RegisterNavigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : BaseActivity<ActivityRegisterBinding, RegisterViewModel>(),
    RegisterNavigator {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = viewDataBinding
        binding.vmRegister = registerViewModel
        registerViewModel.viewState = this

    }

    override fun getLayoutID(): Int {
        return R.layout.activity_register
    }

    override fun makeViewModelProvider(): RegisterViewModel {
        return registerViewModel
    }

    override fun openLogin() {
        val intent = Intent(this, LoginActivity::class.java)
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