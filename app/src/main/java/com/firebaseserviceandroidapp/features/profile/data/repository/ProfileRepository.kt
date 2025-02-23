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
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFireStore: FirebaseFirestore,
    private val context: Context, // Inject Context for SharedPreferences
) {
    // In-memory cache
    private val profileCache = mutableMapOf<String, Map<String, Any>>()

    // SharedPreferences for disk caching
    private val sharedPreferences =
        context.getSharedPreferences("ProfileCache", Context.MODE_PRIVATE)
    private val gson = Gson()

    suspend fun updateProfile(
        name: String,
        title: String,
        description: String,
        imageUri: Uri?,
    ): ProfileResult {
        logD("ProfileFragment", "updateProfile$imageUri")
        val userId = getUserId() ?: return ProfileResult.Error("User not logged in")
        val user = createUserMap(name, title, description, imageUri)
        return try {
            updateUserInFirestore(userId, user)
            updateCaches(userId, user)
            ProfileResult.Success(user)
        } catch (e: Exception) {
            logD("ProfileRepository", "Error updating profile: ${e.message}")
            ProfileResult.Error(e.message ?: "Error updating profile")
        }
    }

    suspend fun getUserProfile(): ProfileResult {
        val userId = getUserId() ?: return ProfileResult.Error("User not logged in")
        return getCachedProfile(userId) ?: fetchProfileFromFirestore(userId)
    }

    private fun getUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

    private fun createUserMap(
        name: String,
        title: String,
        description: String,
        imageUri: Uri?,
    ): MutableMap<String, String> {
        return mutableMapOf(
            "name" to name,
            "title" to title,
            "description" to description
        ).apply {
            imageUri?.let { this["imageUri"] = it.toString() }
        }
    }

    private suspend fun updateUserInFirestore(userId: String, user: Map<String, Any>) {
        firebaseFireStore.collection(Constants.COLLECTION_USERS)
            .document(userId)
            .update(user)
            .await()
    }

    private fun updateCaches(userId: String, user: Map<String, Any>) {
        profileCache[userId] = user
        saveProfileToCache(userId, user)
    }

    private suspend fun getCachedProfile(userId: String): ProfileResult? {
        profileCache[userId]?.let { return ProfileResult.Success(it) }
        val diskCachedProfile = withContext(Dispatchers.IO) { getProfileFromCache(userId) }
        return diskCachedProfile?.let {
            profileCache[userId] = it
            ProfileResult.Success(it)
        }
    }

    private suspend fun fetchProfileFromFirestore(userId: String): ProfileResult {
        return try {
            val profileData = withContext(Dispatchers.IO) {
                firebaseFireStore.collection(Constants.COLLECTION_USERS)
                    .document(userId)
                    .get()
                    .await()
                    .data ?: emptyMap()
            }
            updateCaches(userId, profileData)
            ProfileResult.Success(profileData)
        } catch (e: Exception) {
            logD("ProfileRepository", "Error fetching profile: ${e.message}")
            ProfileResult.Error(e.message ?: "Error fetching profile")
        }
    }

    private fun saveProfileToCache(userId: String, profile: Map<String, Any>) {
        val json = gson.toJson(profile)
        sharedPreferences.edit().putString("profile_$userId", json).apply()
    }

    private fun getProfileFromCache(userId: String): Map<String, Any>? {
        val json = sharedPreferences.getString("profile_$userId", null)
        return json?.let { gson.fromJson(it, Map::class.java) as Map<String, Any> }
    }
    fun clearCache() {
        profileCache.clear()
        sharedPreferences.edit().clear().apply()
    }
}