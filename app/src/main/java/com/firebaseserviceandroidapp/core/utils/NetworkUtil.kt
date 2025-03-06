package com.firebaseserviceandroidapp.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.databinding.ObservableField
import com.firebaseserviceandroidapp.core.constants.Constants
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore

object NetworkUtil {
    var firebaseUser = FirebaseAuth.getInstance().currentUser
    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun createPropertyChangedCallback(errorField: ObservableField<String>) =
        object : androidx.databinding.Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(
                sender: androidx.databinding.Observable?,
                propertyId: Int,
            ) {
                errorField.set(null)
            }
        }

    fun getCollection(collectionName: String)
            : CollectionReference {
        val db = Firebase.firestore
        // val collectionRef = db.collection(ApplicationUser.COLLECTION_NAME)
        return db.collection(collectionName)
    }

    fun getUser(
        uid: String,
        onSuccessListener: OnSuccessListener<DocumentSnapshot>,
        onFailureListener: OnFailureListener,
    ) {

        val collectionRef = getCollection(Constants.COLLECTION_USERS)
        collectionRef.document(uid).get().addOnSuccessListener(onSuccessListener)
            .addOnFailureListener(onFailureListener)
    }
}