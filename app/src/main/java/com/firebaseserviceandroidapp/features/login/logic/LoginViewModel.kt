package com.firebaseserviceandroidapp.features.login.logic

import android.util.Log
import androidx.databinding.ObservableField
import com.firebaseserviceandroidapp.core.base.activity.BaseViewModel
import com.firebaseserviceandroidapp.core.constants.Constants
import com.firebaseserviceandroidapp.core.utils.NetworkUtil.createPropertyChangedCallback
import com.firebaseserviceandroidapp.features.login.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
) : BaseViewModel<LoginViewState>() {

    var email = ObservableField<String>()
    var password = ObservableField<String>()
    val emailError = ObservableField<String>()
    val passwordError = ObservableField<String>()

    init {
        email.addOnPropertyChangedCallback(createPropertyChangedCallback(emailError))
        password.addOnPropertyChangedCallback(createPropertyChangedCallback(passwordError))
    }


    fun openRegister() {
        viewState?.openRegister()
    }

    fun openHome() {
        if (validateInputs()) {
            showLoading.value = true
            loginRepository.login(
                email.get() ?: "",
                password.get() ?: "",
                onSuccess = {
                    showLoading.value = false
                    viewState?.openHome()
                },
                onError = { error -> handleError(error) }
            )
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        if (email.get().isNullOrBlank()) {
            emailError.set(Constants.ERROR_EMAIL)
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.get()).matches()) {
            emailError.set(Constants.ERROR_EMAIL_INVALID)
            isValid = false
        }

        if (password.get().isNullOrBlank()) {
            passwordError.set(Constants.ERROR_PASSWORD)
            isValid = false
        } else password.get()?.length?.let {
            if (it < 8) {
                passwordError.set(Constants.ERROR_PASSWORD_LENGTH)
                isValid = false
            }
        }

        return isValid
    }

    private fun handleError(error: String) {
        // Log the error
        Log.e("RegisterViewModel", error)
        showLoading.value = false
        // Show a message to the user
        messageLiveData.postValue(error)
    }
}