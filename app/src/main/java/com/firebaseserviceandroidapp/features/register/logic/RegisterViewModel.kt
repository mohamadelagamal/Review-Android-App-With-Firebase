package com.firebaseserviceandroidapp.features.register.logic

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.firebaseserviceandroidapp.core.base.activity.BaseViewModel
import com.firebaseserviceandroidapp.core.constants.Constants
import com.firebaseserviceandroidapp.core.utils.NetworkUtil.createPropertyChangedCallback
import com.firebaseserviceandroidapp.features.register.data.models.RegisterResponse
import com.firebaseserviceandroidapp.features.register.data.repository.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerRepository: RegisterRepository) :
    BaseViewModel<RegisterViewState>() {

    var name = ObservableField<String>()
    var title = ObservableField<String>()
    var email = ObservableField<String>()
    var password = ObservableField<String>()

    // error fields
    val emailError = ObservableField<String>()
    val passwordError = ObservableField<String>()
    val nameError = ObservableField<String>()
    val titleError = ObservableField<String>()

    init {
        name.addOnPropertyChangedCallback(createPropertyChangedCallback(nameError))
        title.addOnPropertyChangedCallback(createPropertyChangedCallback(titleError))
        email.addOnPropertyChangedCallback(createPropertyChangedCallback(emailError))
        password.addOnPropertyChangedCallback(createPropertyChangedCallback(passwordError))
    }

    fun register() {
        if (validateInputs()) {
            showLoading.value = true
            val registerResponse = RegisterResponse(
                id = "", // You can generate or assign an ID if needed
                name = name.get() ?: "",
                title = title.get() ?: "",
                email = email.get() ?: "",
                password = password.get() ?: ""
            )
            viewModelScope.launch {
                registerRepository.registerUser(
                    registerResponse.name,
                    registerResponse.title,
                    registerResponse.email,
                    registerResponse.password,
                    onSuccess = {
                        showLoading.value = false
                        viewState?.openHome()
                    },
                    onError = { error -> handleError(error) }
                )
            }
        }
    }

    fun openLogin() {
        viewState?.openLogin()
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        if (name.get().isNullOrBlank()) {
            nameError.set(Constants.ERROR_NAME)
            isValid = false
        } else {
            nameError.set(null)
        }

        if (title.get().isNullOrBlank()) {
            titleError.set(Constants.ERROR_TITLE)
            isValid = false
        } else {
            titleError.set(null)
        }

        if (email.get().isNullOrBlank()) {
            emailError.set(Constants.ERROR_EMAIL)
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.get()).matches()) {
            emailError.set(Constants.ERROR_EMAIL_INVALID)
            isValid = false
        } else {
            emailError.set(null)
        }

        if (password.get().isNullOrBlank()) {
            passwordError.set(Constants.ERROR_PASSWORD)
            isValid = false
        } else password.get()?.length?.let {
            if (it < 8) {
                passwordError.set(Constants.ERROR_PASSWORD_LENGTH)
                isValid = false
            } else {
                passwordError.set(null)
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