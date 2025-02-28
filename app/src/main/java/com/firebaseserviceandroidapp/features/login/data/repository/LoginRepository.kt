package com.firebaseserviceandroidapp.features.login.data.repository

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import com.firebaseserviceandroidapp.R


class LoginRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFireStore: FirebaseFirestore,
    val application: Application
) {

    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit)  {
        // handle login using firebaseAuth and firebaseFirestore
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(
                        application.getString(R.string.validationEmailAndPassword)
                    )
                }
            }

    }
}