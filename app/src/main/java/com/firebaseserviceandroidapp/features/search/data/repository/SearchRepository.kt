package com.firebaseserviceandroidapp.features.search.data.repository

import android.util.Log
import com.firebaseserviceandroidapp.core.constants.Constants
import com.firebaseserviceandroidapp.features.add_note_dialog.data.model.NoteItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
) {
    suspend fun searchItems(query: String): List<NoteItem> {
        val userId = firebaseAuth.currentUser?.uid
            ?: throw Exception("User not authenticated")

        val lowercaseQuery = query.lowercase()

        return try {
            val titleResults = fireStore.collection(Constants.COLLECTION_USERS)
                .document(userId)
                .collection(Constants.COLLECTION_NOTES)
                .orderBy("titleLowercase")
                .startAt(lowercaseQuery)
                .endAt(lowercaseQuery + "\uf8ff")
                .get()
                .await()
                .toObjects(NoteItem::class.java)
                .filter { it.title.lowercase().contains(lowercaseQuery) } // فلترة يدوية

            Log.d("SearchRepository", "Title results: $titleResults")

            val descriptionResults = fireStore.collection(Constants.COLLECTION_USERS)
                .document(userId)
                .collection(Constants.COLLECTION_NOTES)
                .orderBy("descriptionLowercase")
                .startAt(lowercaseQuery)
                .endAt(lowercaseQuery + "\uf8ff")
                .get()
                .await()
                .toObjects(NoteItem::class.java)
                .filter { it.description.lowercase().contains(lowercaseQuery) } // فلترة يدوية

            Log.d("SearchRepository", "Description results: $descriptionResults")

            val results = (titleResults + descriptionResults).distinct()
            Log.d("SearchRepository", "Combined results: $results")
            Log.d("SearchRepository", "Query: $query")

            results
        } catch (e: Exception) {
            Log.e("SearchRepository", "Error searching items", e)
            emptyList()
        }
    }
}