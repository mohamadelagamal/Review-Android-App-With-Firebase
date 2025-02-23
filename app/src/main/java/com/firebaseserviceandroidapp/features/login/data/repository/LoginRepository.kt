package com.firebaseserviceandroidapp.features.login.data.repository

import com.firebaseserviceandroidapp.core.constants.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject


class LoginRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFireStore: FirebaseFirestore
) {

    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit)  {
        // handle login using firebaseAuth and firebaseFirestore
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(Constants.EMAIL_OR_PASSWORD_INVALID)
                }
            }

    }
}