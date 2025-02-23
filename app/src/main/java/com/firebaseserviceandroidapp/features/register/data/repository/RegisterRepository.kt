package com.firebaseserviceandroidapp.features.register.data.repository

import com.firebaseserviceandroidapp.core.constants.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class RegisterRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFireStore: FirebaseFirestore,
) {
    suspend fun registerUser(
        name: String,
        title: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        // register user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = task.result?.user?.uid
                    val user = hashMapOf(
                        "id" to userId,
                        "name" to name,
                        "title" to title,
                        "email" to email,
                        "description" to "Hello, I'm new here!"
                    )
                    firebaseFireStore.collection(Constants.COLLECTION_USERS)
                        .document(userId!!)
                        .set(user)
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener {
                            onError(it.message.toString())
                        }
                } else {
                    onError(task.exception?.message.toString())
                }

            }
    }
}