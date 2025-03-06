package com.firebaseserviceandroidapp.features.profile.data.models

import android.net.Uri

sealed class ProfileResult {
    data class Success(val data: Map<String, Any>) : ProfileResult()
    data class Error(val error: String) : ProfileResult()
}

sealed class ProfileUIState {
    object Loading : ProfileUIState()
    data class Success(
        val name: String,
        val title: String,
        val email: String,
        val description: String,
        val imageUri: Uri?,
    ) : ProfileUIState()
    data class Error(val message: String) : ProfileUIState()
}
