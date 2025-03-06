package com.firebaseserviceandroidapp.features.profile.data.repository

import android.content.Context
import android.net.Uri
import com.firebaseserviceandroidapp.core.constants.Constants
import com.firebaseserviceandroidapp.core.extensions.logD
import com.firebaseserviceandroidapp.features.profile.data.models.ProfileResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFireStore: FirebaseFirestore,
    private val context: Context, // Inject Context for SharedPreferences
) {
    private val cache = mutableMapOf<String, Map<String, Any>>()
    private val prefs = context.getSharedPreferences("ProfileCache", Context.MODE_PRIVATE)
    private val gson = Gson()

    suspend fun updateProfile(
        name: String,
        title: String,
        desc: String,
        imageUri: Uri?,
    ): ProfileResult {
        logD("ProfileRepo", "Updating profile: $imageUri")
        val userId =
            firebaseAuth.currentUser?.uid ?: return ProfileResult.Error("User not logged in")
        val user = mapOf(
            "name" to name,
            "title" to title,
            "description" to desc,
            "imageUri" to saveImage(imageUri)?.toString()
        )
        return runCatching { // run catch block to handle exceptions if any occurs during the execution of the code inside the block
            firebaseFireStore.collection(Constants.COLLECTION_USERS).document(userId).update(user)
                .await()
            cache[userId] = user as Map<String, Any>
            saveToPrefs(userId, user)
            ProfileResult.Success(user)
        }.getOrElse { ProfileResult.Error(it.message ?: "Error updating profile") }
    }

    suspend fun getProfile(): ProfileResult {
        val userId =
            firebaseAuth.currentUser?.uid ?: return ProfileResult.Error("User not logged in")
        cache[userId]?.let { return ProfileResult.Success(it) }
        getFromPrefs(userId)?.let {
            cache[userId] = it
            return ProfileResult.Success(it)
        }
        return fetchProfile(userId)
    }

    private suspend fun fetchProfile(userId: String): ProfileResult {
        return runCatching {
            val data = withContext(Dispatchers.IO) {
                firebaseFireStore.collection(Constants.COLLECTION_USERS).document(userId).get()
                    .await().data
                    ?: emptyMap()
            }
            cache[userId] = data
            saveToPrefs(userId, data)
            ProfileResult.Success(data)
        }.getOrElse { ProfileResult.Error(it.message ?: "Error fetching profile") }
    }

    private fun saveToPrefs(userId: String, data: Map<String, Any>) {
        prefs.edit().putString("profile_$userId", gson.toJson(data)).apply()
    }

    private fun getFromPrefs(userId: String): Map<String, Any>? {
        return prefs.getString("profile_$userId", null)
            ?.let { gson.fromJson(it, Map::class.java) as Map<String, Any> }
    }

    fun clearCache() {
        cache.clear()
        prefs.edit().clear().apply()
    }

    private fun saveImage(uri: Uri?): Uri? {
        if (uri == null) return null
        return runCatching {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.filesDir, "profile.jpg")
            FileOutputStream(file).use { outputStream -> inputStream?.copyTo(outputStream) }
            inputStream?.close()
            Uri.fromFile(file)
        }.getOrNull()
    }
}
